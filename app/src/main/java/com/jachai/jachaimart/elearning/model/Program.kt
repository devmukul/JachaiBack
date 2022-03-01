package com.jachai.jachaimart.elearning.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Program(
    @SerializedName("id")
    @Expose
    var id: String = "",
    @SerializedName("isActive")
    @Expose
    var isActive: Boolean = false,
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("type")
    @Expose
    var type: String = ""
)