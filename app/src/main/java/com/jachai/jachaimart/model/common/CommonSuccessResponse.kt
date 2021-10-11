package com.jachai.jachai_driver.model.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CommonSuccessResponse<T>(
    @SerializedName("success")
    @Expose
    var success: Boolean = false,

    @SerializedName("message")
    @Expose
    var message: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("data")
    @Expose
    var data: T? = null
)
