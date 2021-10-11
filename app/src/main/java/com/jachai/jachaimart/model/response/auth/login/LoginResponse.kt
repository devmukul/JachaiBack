package com.jachai.user.model.response.auth.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @Expose
    @SerializedName("refresh_token")
    var refresh: String? = null,

    @SerializedName("access_token")
    @Expose
    val accessToken: String? = null,

    @SerializedName("detail")
    @Expose
    val details: String? = null,

    @SerializedName("message")
    @Expose
    var message: String? = null
)
