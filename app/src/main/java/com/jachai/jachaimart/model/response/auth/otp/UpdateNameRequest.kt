package com.jachai.user.model.response.auth.otp


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class UpdateNameRequest(
    @SerializedName("fullName")
    @Expose
    var fullName: String
)