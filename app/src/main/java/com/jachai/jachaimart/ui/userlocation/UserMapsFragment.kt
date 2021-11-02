package com.jachai.jachaimart.ui.userlocation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import bd.com.evaly.ehealth.models.common.CurrentLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FragmentUserMapsBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class UserMapsFragment : BaseFragment<FragmentUserMapsBinding>(R.layout.fragment_user_maps),
    OnMapReadyCallback {
    protected lateinit var gMap: GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    override fun initView() {

    }

    override fun subscribeObservers() {

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            this.gMap = map
            gMap.isMyLocationEnabled = true
            gMap.uiSettings.isMyLocationButtonEnabled = true

            fetchCurrentLocation { location: CurrentLocation? ->
                updateMapUI(location)
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
}