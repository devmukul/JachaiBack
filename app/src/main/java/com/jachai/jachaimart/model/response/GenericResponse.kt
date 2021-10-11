package com.jachai.user.model.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GenericResponse(
    @SerializedName("message")
    @Expose
    var message: String? = null,
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int =0
)