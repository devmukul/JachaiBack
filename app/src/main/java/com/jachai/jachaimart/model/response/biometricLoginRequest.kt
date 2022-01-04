package com.jachai.jachaimart.model.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class biometricLoginRequest(
    @SerializedName("deviceId")
    @Expose
    var deviceId: String,
    @SerializedName("secret")
    @Expose
    var secret: String
)