package com.jachai.jachaimart.utils

import androidx.core.content.edit
import com.google.gson.Gson
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.model.response.location.LocationDetails
import com.jachai.jachaimart.utils.constant.CommonConstants
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants

object SharedPreferenceUtil {

    private val preferences = JachaiFoodApplication.preferences
    private val TAG = SharedPreferenceUtil::class.java

    fun getAuthToken() =
        preferences.getString(
            SharedPreferenceConstants.AUTH_TOKEN_KEY,
            CommonConstants.INVALID_ACCESS_TOKEN
        )

    fun getAuthTokenWithOutBearer() =
        preferences.getString(
            SharedPreferenceConstants.AUTH_TOKEN_KEY_WITHOUT_BEARER,
            CommonConstants.INVALID_ACCESS_TOKEN_WITHOUT_BEARER
        )

    fun setAuthToken(token: String?) {
        preferences.edit {
            putString(SharedPreferenceConstants.AUTH_TOKEN_KEY, "Bearer " + token)
            putString(SharedPreferenceConstants.AUTH_TOKEN_KEY_WITHOUT_BEARER, token)
        }
    }

    fun isTokenAvailable() = preferences.contains(SharedPreferenceConstants.AUTH_TOKEN_KEY)

    fun getName() =
        preferences.getString(SharedPreferenceConstants.DRIVER_NAME_KEY, "")

    fun setName(name: String?) {
        preferences.edit {
            putString(SharedPreferenceConstants.DRIVER_NAME_KEY, name)
        }
    }

    fun getDriverId() =
        preferences.getString(SharedPreferenceConstants.DRIVER_ID_KEY, "")

    fun setDriverId(name: String?) {
        preferences.edit {
            putString(SharedPreferenceConstants.DRIVER_ID_KEY, name)
        }
    }

    fun getJCShopId() =
        preferences.getString(SharedPreferenceConstants.JC_SHOP_ID_KEY, null)

    fun setJCShopId(name: String?) {
        preferences.edit {
            putString(SharedPreferenceConstants.JC_SHOP_ID_KEY, name)
        }
    }

    fun getMobileNo() =
        preferences.getString(SharedPreferenceConstants.DRIVER_MOBILE_KEY, "")

    fun setMobileNo(name: String?) {
        preferences.edit {
            putString(SharedPreferenceConstants.DRIVER_MOBILE_KEY, name)
        }
    }

    fun setUserLocation(locationDetails: LocationDetails) {
        preferences.edit {
            val data = Gson().toJson(locationDetails)
            putString(SharedPreferenceConstants.USER_LOCATION_KEY, data)
        }
    }
}