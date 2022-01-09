package com.jachai.jachaimart.model.response.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BiometricRegisterRequest (

        @SerializedName("deviceId")
        @Expose
        var deviceId: String? = null
)
