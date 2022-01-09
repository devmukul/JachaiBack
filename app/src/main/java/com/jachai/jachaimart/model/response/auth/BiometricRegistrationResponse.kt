package com.jachai.jachaimart.model.response.auth


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class BiometricRegistrationResponse(
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("secret")
    @Expose
    var secret: String,
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int
)