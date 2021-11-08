package com.jachai.jachaimart.ui.base


import android.Manifest
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import bd.com.evaly.ehealth.models.common.CurrentLocation
import com.jachai.jachai_driver.utils.JachaiLocationManager
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.Utils
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.JachaiFoodApplication.Companion.compositeDisposable
import com.jachai.jachaimart.JachaiFoodApplication.Companion.socketStatus
import com.jachai.jachaimart.R
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


abstract class BaseActivity<BINDING : ViewDataBinding>(@LayoutRes val layoutId: Int) :
    AppCompatActivity() {

    protected lateinit var binding: BINDING

    private lateinit var progressDialog: ProgressDialog
    private lateinit var locationManager: JachaiLocationManager
    val channelId = UUID.randomUUID().toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_JachaiMart_NoActionBar)
        super.onCreate(savedInstanceState)

        locationManager = JachaiLocationManager(this)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        setContentView(binding.root)
    }

    protected fun <VIEW_MODEL : ViewModel> bindViewModel(viewModel: VIEW_MODEL) {
        binding.setVariable(1, viewModel)
    }

    @TargetApi(Build.VERSION_CODES.M)
    open fun hasPermission(permission: String?): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
    }

    open fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard() {
        val imm = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun isNetworkConnected(): Boolean = isConnectionAvailable()

    fun openActivityOnTokenExpire() {
        // startActivity(LoginActivity.newIntent(this));
        finish()
    }

    /**
     * Navigates to given destination with [defaultDestination] and [deepLink]
     * [deepLink] will get priority over [defaultDestination]
     * @param defaultDestination is the direction of the destination
     * @param deepLink is the deep link
     * @param navOptions is the [NavOptions] that is expected to run while navigation
     */
    fun navigateTo(
        defaultDestination: NavDirections? = null,
        deepLink: String? = null,
        navOptions: NavOptions? = null
    ) {
        if (defaultDestination == null && deepLink == null) {
            JachaiLog.e("Error", "navigateTo: No defaultDestinationId or deepLink provided")
            return
        }
//        try {
//            val controller = findNavController(R.id.mobileNumber)
//            if (deepLink != null) {
//                processDeepLink(this, controller, Uri.parse(deepLink), navOptions)
//                return
//            }
//            defaultDestination?.let { controller.navigate(it, navOptions) }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    /**
     * This method will help to find app deeplink, if found it will safely navigate there
     * or other wise will try validated the deeplink is valid URL or not,
     * if valid then safely open the URL
     * @param context is context of the current activity
     * @param controller is app navigation controller from nav host.
     * @param deepLink will contain app deepLink or web link.
     */
    private fun processDeepLink(
        context: Context,
        controller: NavController,
        deepLink: Uri,
        navOptions: NavOptions? = null
    ) {
        if (controller.graph.hasDeepLink(deepLink)) {
            controller.navigate(deepLink, navOptions)
        } else if (URLUtil.isValidUrl(deepLink.toString())) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, deepLink)
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun showLoader() {
        progressDialog = ProgressDialog(this@BaseActivity)
        progressDialog.setMessage("Please wait while loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    fun showLoader(loadinText: String) {
        progressDialog = ProgressDialog(this@BaseActivity)
        progressDialog.setMessage(loadinText)
        progressDialog.show()
    }

    private fun createNotification(title: String, description: String): Notification.Builder {
        //for greater than android O
        //for greater than android O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "TRIP_NOTIFICATION",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Notification base on trip"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(
                this@BaseActivity,
                channelId
            )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(description)

        } else {
            TODO("VERSION.SDK_INT < O")
        }

        return builder
    }

    fun notifyUser(title: String, description: String) {
        with(NotificationManagerCompat.from(this@BaseActivity)) {
            // notificationId is a unique int for each notification that you must define
            notify(NotificationID.iD, createNotification(title, description).build())
        }

    }

    object NotificationID {
        private val c: AtomicInteger = AtomicInteger(0)
        val iD: Int
            get() = c.incrementAndGet()
    }


    fun dismissLoader() {
        try {
            if (this::progressDialog.isInitialized && progressDialog.isShowing)
                progressDialog.dismiss()

        } catch (exp: Exception) {
        }
    }

    fun fetchCurrentLocation(onLocationResultUpdate: (location: CurrentLocation?) -> Unit) =
        locationManager.fetchCurrentLocation(onLocationResultUpdate)

    override fun onDestroy() {
        locationManager.removeLocationCallBack()
        super.onDestroy()
    }


    open fun connectStomp() {
        JachaiFoodApplication.mStompClient.withClientHeartbeat(10000).withServerHeartbeat(10000)
        resetSubscriptions()
        val dispLifecycle = JachaiFoodApplication.mStompClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lifecycleEvent: LifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> {
                        JachaiLog.e(BaseFragment.TAG, "Stomp connection opened")
                        socketStatus.postValue(true)
                    }
                    LifecycleEvent.Type.ERROR -> {
                        socketStatus.postValue(false)
                        JachaiLog.e(
                            BaseFragment.TAG,
                            "Stomp connection error",
                            lifecycleEvent.exception
                        )

                    }
                    LifecycleEvent.Type.CLOSED -> {
                        socketStatus.postValue(false)
                        JachaiLog.e(BaseFragment.TAG, "Stomp connection closed")
                        resetSubscriptions()


                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                        socketStatus.postValue(false)
                        JachaiLog.e(
                            BaseFragment.TAG,
                            "Stomp failed server heartbeat"
                        )
                    }
                }
            }
        compositeDisposable?.add(dispLifecycle)
        JachaiFoodApplication.mStompClient.connect()


    }

    private fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable?.dispose()
        }
        compositeDisposable = CompositeDisposable()
    }

    open fun disconnectStomp() {
        if (JachaiFoodApplication.mStompClient.isConnected) {
            JachaiFoodApplication.mStompClient.disconnect()
        }
    }

    open fun reconnectStomp() {

        JachaiFoodApplication.mStompClient.reconnect()

    }

    protected open fun applySchedulers(): CompletableTransformer? {
        return CompletableTransformer { upstream: Completable ->
            upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    open fun destroyPing() {
        if (JachaiFoodApplication.mRestPingDisposable != null) JachaiFoodApplication.mRestPingDisposable?.dispose()
        if (compositeDisposable != null) compositeDisposable?.dispose()
    }

    override fun onStop() {
        super.onStop()

    }

    open fun phoneCall(phoneNumber: String) {


        val number = "+$phoneNumber"
        val intent = Intent(Intent.ACTION_CALL);
        intent.data = Uri.parse("tel:$number")

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CALL_PHONE
                )
            ) {
            } else {
                val MY_PERMISSIONS_REQUEST_CALL_PHONE = 0
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    MY_PERMISSIONS_REQUEST_CALL_PHONE
                )
            }
        }
        try {
            startActivity(intent)

        } catch (e: Exception) {
            Utils.longToast(applicationContext, "Phone number not found")
        }
    }

    protected abstract fun initView()
    protected abstract fun initToolbar()
    protected abstract fun subscribeObservers()
    abstract fun hideBottomNavigation()
    abstract fun showBottomNavigation()
}
