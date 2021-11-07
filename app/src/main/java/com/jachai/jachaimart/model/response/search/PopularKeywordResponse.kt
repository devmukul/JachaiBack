package com.jachai.jachaimart.model.response.search


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class PopularKeywordResponse(
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("popular")
    @Expose
    var popular: List<String>,
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int
)