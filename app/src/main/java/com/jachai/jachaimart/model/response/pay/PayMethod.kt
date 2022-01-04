package com.jachai.jachaimart.model.response.pay

import com.google.gson.annotations.SerializedName

data class PayMethod(
    @SerializedName("description")
    var description: String,
    @SerializedName("logo")
    var logo: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("type")
    var type: String
)