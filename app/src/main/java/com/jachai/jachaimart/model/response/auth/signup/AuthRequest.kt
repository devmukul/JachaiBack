package com.jachai.jachaimart.model.response.auth.signup

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class AuthRequest {

        @SerializedName("mobileNumber")
        @Expose
        var mobileNumber: String? = null

        @SerializedName("otp")
        @Expose
        var otp: String? = null
}
