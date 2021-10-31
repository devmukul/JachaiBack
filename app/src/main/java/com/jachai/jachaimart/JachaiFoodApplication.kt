package com.jachai.jachaimart

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import androidx.multidex.MultiDexApplication
import com.jachai.jachaimart.database.AppDatabase
import com.jachai.jachaimart.manager.smshelper.AppSignatureHelper
import com.jachai.jachaimart.utils.HttpLogger
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import com.jachai.jachaimart.utils.constant.ApiConstants
import com.jachai.jachaimart.utils.constant.CommonConstants
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import ua.naiksoftware.stomp.StompClient
import java.util.concurrent.TimeUnit

class JachaiFoodApplication : MultiDexApplication(), LifecycleObserver {


    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val appSignature = AppSignatureHelper(this)
        Log.v("AppSignature>> ", appSignature.appSignatures.toString())

        instance = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        // Setting up preferences
        preferences = getSharedPreferences(
            SharedPreferenceConstants.TAG_JACHAI,
            Context.MODE_PRIVATE
        )

        //Database
        mDatabase = AppDatabase.getAppDatabase(this)

//        mStompClient = Stomp.over(
//            Stomp.ConnectionProvider.OKHTTP,
//            ApiConstants.JACHAI_BASE_URL_WEBSOCKET
//        )

        setupLogger()


    }

    private fun setupLogger() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(5)
            .tag("eHealth_Log:")
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
        Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                Logger.log(priority, tag, message, t)
            }
        })
    }

    companion object {
        private val TAG = JachaiFoodApplication::class.java.simpleName
        lateinit var instance: JachaiFoodApplication
            private set

        lateinit var preferences: SharedPreferences
            private set

        lateinit var mStompClient: StompClient
            private set

        lateinit var mDatabase: AppDatabase
            private set


        var compositeDisposable: CompositeDisposable? = null

        var mRestPingDisposable: Disposable? = null

        var socketStatus = MutableLiveData<Boolean>()

        private fun logger(): HttpLoggingInterceptor {
            val logger = HttpLoggingInterceptor(HttpLogger())
            logger.level = HttpLoggingInterceptor.Level.BODY
            return logger
        }

        private val okHttpClient = OkHttpClient.Builder()
            .readTimeout(CommonConstants.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(logger())
            .addNetworkInterceptor {
                it.proceed(
                    it.request().newBuilder()
                        .header(
                            ApiConstants.AUTHORIZATION_KEY,
                            SharedPreferenceUtil.getAuthToken()!!
                        )
                        .header(
                            ApiConstants.USER_AGENT_HEADER,
                            ApiConstants.USER_AGENT_MOBILE_ANDROID
                        )
                        .build()
                )
            }
            .addInterceptor(Interceptor { chain ->
                val request: Request = chain.request()
                val response = chain.proceed(request)

                // todo deal with the issues the way you need to
                if (response.code == HttpStatusCode.HTTP_UNAUTHORIZED) {
                    return@Interceptor response
                }
                response
            })
            .build()

        val AUTH_RETROFIT = Retrofit.Builder()
            .baseUrl(ApiConstants.JACHAI_BASE_URL_AUTH)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(CommonConstants.DEFAULT_NON_NULL_GSON))
            .build()

        val CATELOGREADER_RETROFIT = Retrofit.Builder()
            .baseUrl(ApiConstants.JACHAI_BASE_URL_CATELOGREADER)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(CommonConstants.DEFAULT_NON_NULL_GSON))
            .build()
        val ORDER_RETROFIT = Retrofit.Builder()
            .baseUrl(ApiConstants.JACHAI_BASE_URL_ORDER)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(CommonConstants.DEFAULT_NON_NULL_GSON))
            .build()


        fun getAppContext(): JachaiFoodApplication {
            return instance
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
    }

}