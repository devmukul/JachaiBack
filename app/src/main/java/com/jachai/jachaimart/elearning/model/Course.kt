package com.jachai.jachaimart.elearning.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Course(
    @SerializedName("courseDurationInDay")
    @Expose
    var courseDurationInDay: Int? = null,
    @SerializedName("description")
    @Expose
    var description: String = "",
    @SerializedName("durationInMinutes")
    @Expose
    var durationInMinutes: Int? = null,
    @SerializedName("id")
    @Expose
    var id: String = "",
    @SerializedName("isActive")
    @Expose
    var isActive: Boolean = false,
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("price")
    @Expose
    var price: Price? = null,
    @SerializedName("program")
    @Expose
    var program: Program? = null,
    @SerializedName("programId")
    @Expose
    var programId: String? = null,
    @SerializedName("teachers")
    @Expose
    var teachers: Any? = null,
    @SerializedName("totalCheatSheet")
    @Expose
    var totalCheatSheet: Int? = null,
    @SerializedName("totalEnrolled")
    @Expose
    var totalEnrolled: Int = 0,
    @SerializedName("totalLectures")
    @Expose
    var totalLectures: Int? = null,
    @SerializedName("totalNotes")
    @Expose
    var totalNotes: Int? = null,
    @SerializedName("totalQuiz")
    @Expose
    var totalQuiz: Int? = null
)