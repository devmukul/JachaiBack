package com.jachai.jachaimart.elearning.model.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.jachai.jachaimart.elearning.model.Discipline

data class DiciplineListResponse(
    @SerializedName("disciplines")
    @Expose
    var disciplines: MutableList<Discipline> = mutableListOf(),
    @SerializedName("message")
    @Expose
    var message: String = "",
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int = 0
)