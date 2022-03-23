package com.jachai.jachaimart.elearning.model.response

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.jachai.jachaimart.elearning.model.Course

data class CourseListResponse(
    @SerializedName("courses")
    @Expose
    var courses: List<Course> = listOf(),
    @SerializedName("currentPageNumber")
    @Expose
    var currentPageNumber: Int = 0,
    @SerializedName("first")
    @Expose
    var first: Boolean = false,
    @SerializedName("last")
    @Expose
    var last: Boolean = false,
    @SerializedName("message")
    @Expose
    var message: String = "",
    @SerializedName("numberOfElements")
    @Expose
    var numberOfElements: Int = 0,
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int = 0,
    @SerializedName("totalElements")
    @Expose
    var totalElements: Int = 0,
    @SerializedName("totalPages")
    @Expose
    var totalPages: Int = 0
)