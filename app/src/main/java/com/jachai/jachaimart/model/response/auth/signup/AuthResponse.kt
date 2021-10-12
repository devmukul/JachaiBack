package com.jachai.user.model.response.auth.signup


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class AuthResponse(
    @SerializedName("balance")
    @Expose
    var balance: Int? = null,
    @SerializedName("message")
    @Expose
    var message: String? = null,
    @SerializedName("mobileNumber")
    @Expose
    var mobileNumber: String? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("rating")
    @Expose
    var rating: Int? = null,
    @SerializedName("status")
    @Expose
    var status: String? = null,
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null,
    @SerializedName("token")
    @Expose
    var token: String? = null
)