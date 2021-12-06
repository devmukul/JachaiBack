package com.jachai.jachaimart.model.notification


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class NotificationRegisterRequest(
    @SerializedName("deviceId")
    @Expose
    var deviceId: String,
    @SerializedName("deviceType")
    @Expose
    var deviceType: String,
    @SerializedName("model")
    @Expose
    var model: String,
    @SerializedName("pushToken")
    @Expose
    var pushToken: String
)