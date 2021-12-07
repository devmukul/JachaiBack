package com.jachai.jachaimart

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.jachai.jachai_driver.utils.*
import com.jachai.jachaimart.databinding.ActivityMainBinding
import com.jachai.jachaimart.databinding.ContentMainBinding
import com.jachai.jachaimart.model.notification.NotificationRegisterRequest
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.home.BannerResponse
import com.jachai.jachaimart.ui.base.BaseActivity
import com.jachai.jachaimart.ui.home.HomeViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.constant.CommonConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : BaseActivity<ContentMainBinding>(R.layout.content_main)  {

//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var locationManager: JachaiLocationManager
//    val channelId = UUID.randomUUID().toString()

    private val notificationsService = RetrofitConfig.notificationsService
    private var registerCall: Call<GenericResponse>? = null

//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        locationManager = JachaiLocationManager(this)
//        setContentView(binding.root)

//        val drawerLayout: DrawerLayout = binding.drawerLayout
//        val navView: NavigationView = binding.navView
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
//            ), drawerLayout
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            requestRegister (token)
            Log.d("Jachai_FCM", "sendRegistrationTokenToServer($token)")
        })
    }

    override fun initView() {

    }

    override fun initToolbar() {

    }

    override fun subscribeObservers() {

    }

    override fun hideBottomNavigation() {

    }

    override fun showBottomNavigation() {

    }

    fun requestRegister(fcmToken: String) {
        try {
            if (registerCall != null) {
                return
            }
            val notificationRegisterRequest = NotificationRegisterRequest(this.getDeviceId(), "Android", "${Build.BRAND}_${Build.MODEL}", fcmToken)

            registerCall = notificationsService.registerDevice(notificationRegisterRequest)

            registerCall?.enqueue(object : Callback<GenericResponse> {
                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {
                    registerCall = null
                    JachaiLog.d(HomeViewModel.TAG, response.body().toString())
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    registerCall = null

                }
            })

        } catch (ex: Exception) {
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
        }

    }




}