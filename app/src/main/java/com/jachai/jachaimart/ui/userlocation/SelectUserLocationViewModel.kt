package com.jachai.jachaimart.ui.userlocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachaimart.JachaiFoodApplication.Companion.placesClient
import com.jachai.jachaimart.model.response.location.LocationDetails

class SelectUserLocationViewModel : ViewModel() {
    val TAG = SelectUserLocationViewModel::class.java
    val token = AutocompleteSessionToken.newInstance()

    var successResponseLiveData = MutableLiveData<List<AutocompletePrediction>?>()
    var successLocationDetailsResponseLiveData = MutableLiveData<LocationDetails?>()


    fun findAddress(query: String?) {
        if (query != null) {

            val request =
                FindAutocompletePredictionsRequest.builder()
                    // Call either setLocationBias() OR setLocationRestriction().
                    //.setLocationRestriction(bounds)
                    .setOrigin(LatLng(23.7276, 90.4102264))
                    .setCountries("BD")
                    .setTypeFilter(TypeFilter.ADDRESS)
                    .setSessionToken(token)
                    .setQuery(query)
                    .build()
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->


                    successResponseLiveData.postValue(response.autocompletePredictions)
                }.addOnFailureListener { exception: Exception? ->
                    if (exception is ApiException) {
                        JachaiLog.e(TAG, "Place not found: " + exception.statusCode)
                    }
                }

        } else {
            successResponseLiveData.postValue(emptyList())
        }


    }


    fun findPlaceDetailsByID(
        placeId: String,
        data: AutocompletePrediction
    ) {

        var mPlace: Place? = null
        val placeFields = listOf(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.ADDRESS)

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->

                mPlace = response.place
                val locationDetails = LocationDetails()


                locationDetails.primaryAddress = data.getPrimaryText(null).toString()
                locationDetails.fullAddress = data.getFullText(null).toString()
                locationDetails.secondaryAddress = data.getSecondaryText(null).toString()

                locationDetails.latitude = response.place.latLng?.latitude
                locationDetails.longitude = response.place.latLng?.longitude
                locationDetails.placeId = data.placeId



                successLocationDetailsResponseLiveData.postValue(locationDetails)


            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {

                    val statusCode = exception.statusCode
                }

            }
    }
}