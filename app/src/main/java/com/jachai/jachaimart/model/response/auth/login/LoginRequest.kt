package com.jachai.user.model.response.auth.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("mobileNumber")
    @Expose var mobileNumber: String? = null,
    @SerializedName("password")
    @Expose var password: String? = null
)
