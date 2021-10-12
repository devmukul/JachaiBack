package com.jachai.user.model.response.auth.otp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OtpRequest(
    @SerializedName("mobileNumber")
    @Expose var mobileNumber: String? = null
)