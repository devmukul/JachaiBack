package com.jachai.jachaimart.model.response.search


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Popular(
    @SerializedName("name")
    @Expose
    var name: String
)