package com.jachai.jachaimart

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import androidx.multidex.MultiDexApplication
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.jachai.jachai_driver.utils.getDeviceId
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

class JachaiApplication : MultiDexApplication(), LifecycleObserver {


    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        Places.initialize(applicationContext, JACHAI_MAP_KEY)
        val appSignature = AppSignatureHelper(this)

        instance = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        // Setting up preferences
        preferences = getSharedPreferences(
            SharedPreferenceConstants.TAG_JACHAI,
            Context.MODE_PRIVATE
        )

        //Database
        mDatabase = AppDatabase.getAppDatabase(this)
//        placesClient = Places.createClient(this)

        setupLogger()

    }

    private fun setupLogger() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(5)
            .tag("Jachai_Log:")
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
        private val TAG = JachaiApplication::class.java.simpleName
        lateinit var instance: JachaiApplication
            private set

        lateinit var preferences: SharedPreferences
            private set

        lateinit var mStompClient: StompClient
            private set

        lateinit var mDatabase: AppDatabase
            private set
//
//        lateinit var placesClient: PlacesClient
//            private set


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
                        ).header(
                            ApiConstants.DEVICE_NAME_TYPE,
                            "${Build.BRAND}_${Build.MODEL}"
                        )
                        .header(
                            ApiConstants.DEVICE_TYPE_TYPE,
                            ApiConstants.DEVICE_TYPE
                        )
                        .header(
                            ApiConstants.DEVICE_ID_TYPE,
                            getAppContext().getDeviceId()
                        )
                        .header(
                            ApiConstants.APP_VERSION_TYPE,
                            ApiConstants.APP_VERSION
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
        val DRIVER_RETROFIT = Retrofit.Builder()
            .baseUrl(ApiConstants.JACHAI_BASE_URL_DRIVER)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(CommonConstants.DEFAULT_NON_NULL_GSON))
            .build()
        val PAYMENT_RETROFIT = Retrofit.Builder()
            .baseUrl(ApiConstants.JACHAI_BASE_URL_PAYMENT)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(CommonConstants.DEFAULT_NON_NULL_GSON))
            .build()

        val NOTIFICATIONS_RETROFIT = Retrofit.Builder()
            .baseUrl(ApiConstants.JACHAI_BASE_URL_NOTIFICATIONS)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(CommonConstants.DEFAULT_NON_NULL_GSON))
            .build()

        val JACHAI_MAP_RETROFIT: Retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.JACHAI_BASE_URL_MAP)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(CommonConstants.DEFAULT_NON_NULL_GSON))
            .build()


        fun getAppContext(): JachaiApplication {
            return instance
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
    }

    fun launchLoginPage() {
        SharedPreferenceUtil.clearAllPreferences();
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }


}