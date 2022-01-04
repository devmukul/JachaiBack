package com.jachai.jachaimart.model.map


import com.google.gson.annotations.SerializedName

data class AutoCompleteBariKoiResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("places")
    var places: List<BariKoiPlace>? = null,
    @SerializedName("statusCode")
    var statusCode: Int? = null
)