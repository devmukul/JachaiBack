package com.jachai.jachaimart.elearning.model.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.jachai.jachaimart.elearning.model.Program

data class ProgramListResponse(
    @SerializedName("message")
    @Expose
    var message: String = "",
    @SerializedName("programs")
    @Expose
    var programs: MutableList<Program> = mutableListOf(),
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int = 0
)