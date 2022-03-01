package com.jachai.jachaimart.elearning.model.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.jachai.jachaimart.elearning.model.Curriculum

data class ClassListResponse(
    @SerializedName("curriculums")
    @Expose
    var curriculums: MutableList<Curriculum> = mutableListOf(),
    @SerializedName("message")
    @Expose
    var message: String = "",
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int = 0
)