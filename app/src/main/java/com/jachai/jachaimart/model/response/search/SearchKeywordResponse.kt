package com.jachai.jachaimart.model.response.search


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SearchKeywordResponse(
    @SerializedName("popular")
    @Expose
    var keys: List<String>,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int
)