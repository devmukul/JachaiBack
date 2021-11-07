package com.jachai.jachaimart.model.response.search


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Key(
    @SerializedName("id")
    @Expose
    var id: String,
    @SerializedName("keyword")
    @Expose
    var keyword: String,
    @SerializedName("name")
    @Expose
    var name: String
)