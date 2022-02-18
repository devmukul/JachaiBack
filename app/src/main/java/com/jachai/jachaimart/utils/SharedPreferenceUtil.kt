package com.jachai.jachaimart.utils

import androidx.core.content.edit
import com.google.gson.Gson
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.model.response.grocery.Hub
import com.jachai.jachaimart.model.response.grocery.Shop
import com.jachai.jachaimart.model.response.location.LocationDetails
import com.jachai.jachaimart.model.response.promo.PromoValidationResponse
import com.jachai.jachaimart.utils.constant.CommonConstants
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants

object SharedPreferenceUtil {

    private val preferences = JachaiApplication.preferences
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

    fun isNameAvailable() = preferences.contains(SharedPreferenceConstants.DRIVER_NAME_KEY)

    fun isFreshLogin() =
        preferences.getBoolean(SharedPreferenceConstants.IS_FRESH_LOGIN, true)

    fun setFreshLogin(isFirst: Boolean) {
        preferences.edit {
            putBoolean(SharedPreferenceConstants.IS_FRESH_LOGIN, isFirst)
        }
    }

    fun getJCShopId() =
        preferences.getString(SharedPreferenceConstants.JC_SHOP_ID_KEY, null)

    fun setJCShopId(name: String?) {
        preferences.edit {
            putString(SharedPreferenceConstants.JC_SHOP_ID_KEY, name)
        }
    }

    fun getJCHubId() =
        preferences.getString(SharedPreferenceConstants.JC_Hub_ID_KEY, null)

    fun setJCHubId(name: String?) {
        if (name == null) {
            removeJCHubId()
            removeNearestHub()
        } else {
            preferences.edit {
                putString(SharedPreferenceConstants.JC_Hub_ID_KEY, name)
            }
        }
    }

    fun removeJCHubId() {
        preferences.edit().remove(SharedPreferenceConstants.JC_Hub_ID_KEY).commit()
    }

    fun isJCShopAvailable() = preferences.contains(SharedPreferenceConstants.JC_SHOP_ID_KEY)
    fun isJCHubAvailable() = preferences.contains(SharedPreferenceConstants.JC_Hub_ID_KEY)

    fun getMobileNo() =
        preferences.getString(SharedPreferenceConstants.DRIVER_MOBILE_KEY, "")

    fun setMobileNo(name: String?) {
        preferences.edit {
            putString(SharedPreferenceConstants.DRIVER_MOBILE_KEY, name)
        }
    }

    fun isMobileAvailable() = preferences.contains(SharedPreferenceConstants.DRIVER_MOBILE_KEY)

    fun setUserLocation(locationDetails: LocationDetails) {
        preferences.edit {
            val data = Gson().toJson(locationDetails)
            putString(SharedPreferenceConstants.USER_LOCATION_KEY, data)
        }
    }

    fun getUserLocation(): LocationDetails {
        val data = preferences.getString(SharedPreferenceConstants.USER_LOCATION_KEY, "")
        return Gson().fromJson(data, LocationDetails::class.java)
    }

    fun saveAddressPosition(position: Int) {
        preferences.edit {
            putInt(SharedPreferenceConstants.JC_ADDRESS_KEY, position)
        }
    }

    fun saveDeliveryAddress(address: Address) {
        preferences.edit {
            val data = Gson().toJson(address)
            putString(SharedPreferenceConstants.USER_ADDRESS_KEY, data)
        }
    }

    fun setConfirmDeliveryAddress(isConfirm: Boolean) {
        preferences.edit {
            putBoolean(SharedPreferenceConstants.JC_IS_CONFIRM_KEY, isConfirm)
        }
    }

    fun isConfirmDeliveryAddress(): Boolean {
        return preferences.getBoolean(SharedPreferenceConstants.JC_IS_CONFIRM_KEY, false)
    }


    fun getDeliveryAddress(): Address? {
        val data = preferences.getString(SharedPreferenceConstants.USER_ADDRESS_KEY, null)
        return if (data != null) {
            Gson().fromJson(data, Address::class.java)
        } else {
            null
        }

    }

    fun removeDeliveryAddress() {
        preferences.edit().remove(SharedPreferenceConstants.USER_ADDRESS_KEY).commit()
    }

    fun saveCurrentAddress(address: Address?) {
        preferences.edit {
            val data = Gson().toJson(address)
            putString(SharedPreferenceConstants.USER_CURRENT_ADDRESS_KEY, data)
        }
    }

    fun getCurrentAddress(): Address? {
        val data = preferences.getString(SharedPreferenceConstants.USER_CURRENT_ADDRESS_KEY, null)

        return if (data != null) {
            Gson().fromJson(data, Address::class.java)
        } else {
            null
        }
    }

    fun saveNotes(note: String) {
        preferences.edit {
            putString(SharedPreferenceConstants.SAVE_NOTE_KEY, note)
        }
    }

    fun getNotes() =
        preferences.getString(SharedPreferenceConstants.SAVE_NOTE_KEY, "n/a")

//    fun saveNearestShop(shop: Shop) {
//        preferences.edit {
//            val data = Gson().toJson(shop)
//            putString(SharedPreferenceConstants.SAVE_SHOP_KEY, data)
//        }
//    }
//
//    fun getNearestShop(): Shop? {
//        val data = preferences.getString(SharedPreferenceConstants.SAVE_SHOP_KEY, null)
//
//        return if (data != null) {
//            Gson().fromJson(data, Shop::class.java)
//        } else {
//            null
//        }
//    }

    fun saveNearestHub(hub: Hub) {
        preferences.edit {
            val data = Gson().toJson(hub)
            putString(SharedPreferenceConstants.SAVE_HUB_KEY, data)
        }
    }

    fun getNearestHub(): Hub? {
        val data = preferences.getString(SharedPreferenceConstants.SAVE_HUB_KEY, null)

        return if (data != null) {
            Gson().fromJson(data, Hub::class.java)
        } else {
            null
        }
    }

    fun removeNearestHub() {
        preferences.edit().remove(SharedPreferenceConstants.SAVE_HUB_KEY).commit()
    }


    fun clearAllPreferences() {
        preferences.edit().clear().apply()
    }

    //promos
    fun savePromoApplied(promoValidationResponse: PromoValidationResponse) {
        preferences.edit {
            val data = Gson().toJson(promoValidationResponse)
            putString(SharedPreferenceConstants.APPLIED_PROMO, data)
        }
    }

    fun getAppliedPromo(): PromoValidationResponse? {
        val data = preferences.getString(SharedPreferenceConstants.APPLIED_PROMO, null)

        return if (data != null) {
            Gson().fromJson(data, PromoValidationResponse::class.java)
        } else {
            null
        }
    }

    fun removeAppliedPromo() {
        preferences.edit().remove(SharedPreferenceConstants.APPLIED_PROMO).apply()
    }


}