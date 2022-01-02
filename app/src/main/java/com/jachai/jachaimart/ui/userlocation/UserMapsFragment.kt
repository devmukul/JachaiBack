package com.jachai.jachaimart.ui.userlocation


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import bd.com.evaly.ehealth.models.common.CurrentLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FragmentUserMapsBinding
import com.jachai.jachaimart.model.response.location.LocationDetails
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.utils.SharedPreferenceUtil


class UserMapsFragment : BaseFragment<FragmentUserMapsBinding>(R.layout.fragment_user_maps),
    OnMapReadyCallback {
    private lateinit var gMap: GoogleMap
    private var mapFragment: SupportMapFragment? = null
    private var userCurrentLocation: CurrentLocation? = null
    private lateinit var nowLocation: CurrentLocation
    private val args: UserMapsFragmentArgs by navArgs()
    private val viewModel: UserMapsViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initView()
        subscribeObservers()

        try {

            val locationDetails = args.mLocation
            if (locationDetails != null) {
                userCurrentLocation = CurrentLocation(
                    locationDetails.latitude!!,
                    locationDetails.longitude!!, locationDetails.fullAddress!!
                )
                val mLocationDetails = LocationDetails()
                mLocationDetails.latitude = locationDetails.latitude
                mLocationDetails.longitude = locationDetails.longitude
                mLocationDetails.fullAddress = locationDetails.fullAddress

                SharedPreferenceUtil.setUserLocation(locationDetails = mLocationDetails)


            }

        } catch (ex: Exception) {

        }


    }

    override fun initView() {

        binding.apply {
            toolbarMain.title.text = "Select Bari location"
            toolbarMain.back.setOnClickListener {
                navController.popBackStack()
            }
            searchLocation.setOnClickListener {
                val action =
                    UserMapsFragmentDirections.actionUserMapsFragmentToSelectUserLocationWithBariKoiFragment()
                action.isFromFragment = args.isFromFragment
                navController.navigate(action)

            }
            confrimButton.setOnClickListener {
                try {
                    val action =
                        UserMapsFragmentDirections.actionUserMapsFragmentToAddressDetailsFragment()
                    action.locationDetails = SharedPreferenceUtil.getUserLocation()
                    action.isFromFragment = args.isFromFragment
                    navController.navigate(action)
                } catch (ex: Exception) {
                    showShortToast("Location is not working correctly. Please try again.")
                }

            }
        }

    }

    override fun subscribeObservers() {

        viewModel.successUserAddressData.observe(viewLifecycleOwner) {
            if (it != null) {
                var locationDetails = LocationDetails()
                locationDetails.latitude = it.latitude
                locationDetails.longitude = it.longitude
                locationDetails.fullAddress = it.address

                SharedPreferenceUtil.setUserLocation(locationDetails = locationDetails)
                binding.searchLocation.text = locationDetails.fullAddress


            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mView = null
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            this.gMap = map
            gMap.clear()
            gMap.isMyLocationEnabled = true
            gMap.uiSettings.isMyLocationButtonEnabled = true



            fetchCurrentLocation { location: CurrentLocation? ->

                if (userCurrentLocation == null) {
                    startLocationService(location)

                    if (location != null) {
                        val mLocationDetails = LocationDetails()
                        mLocationDetails.latitude = location.latitude
                        mLocationDetails.longitude = location.longitude
                        mLocationDetails.fullAddress = location.address
                        SharedPreferenceUtil.setUserLocation(locationDetails = mLocationDetails)
                    }


                } else {
                    startLocationService(userCurrentLocation)
                }
            }


        }

    }

    private fun updateMapUI(currentLocation: CurrentLocation?) {
        if (currentLocation != null) {

            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordToLatLog(currentLocation), 14f))


        } else {
            gMap.uiSettings.isMyLocationButtonEnabled = false

        }
    }

    private fun coordToLatLog(geoCoordinates: CurrentLocation): LatLng? {
        return LatLng(geoCoordinates.latitude, geoCoordinates.longitude)
    }

    private fun startLocationService(location: CurrentLocation?) {
        nowLocation = location!!
        val latLng = location?.let {
            LatLng(
                location.latitude,
                it.longitude
            )
        } //your lat lng

        val marker = gMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_location))
        )
//        marker.position = gMap.cameraPosition.target

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
        gMap.animateCamera(CameraUpdateFactory.zoomTo(15F), 1000, null)
        gMap.setOnCameraMoveListener(object : GoogleMap.OnCameraMoveListener {
            override fun onCameraMove() {
//                val midLatLan = gMap.cameraPosition.target
//                marker.position = midLatLan
                marker.isVisible = false
                if (!binding.staticGps.isVisible) {
                    binding.staticGps.visibility = View.VISIBLE

                }


            }
        })

        gMap.setOnCameraIdleListener(object : GoogleMap.OnCameraIdleListener {
            override fun onCameraIdle() {
                marker.isVisible = true
                binding.staticGps.visibility = View.VISIBLE
                binding.staticGps.visibility = View.GONE

                nowLocation.latitude = marker.position.latitude
                nowLocation.longitude = marker.position.longitude

                val midLatLan = gMap.cameraPosition.target
                marker.position = midLatLan

                viewModel.getAddressFromLatLan(requireContext(), nowLocation)

            }
        })


    }

    // convert drawable into bitmapFactory for google map
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }


}