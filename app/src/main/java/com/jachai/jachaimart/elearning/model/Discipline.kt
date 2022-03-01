package com.jachai.jachaimart.elearning.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Discipline(
    @SerializedName("createdAt")
    @Expose
    var createdAt: String = "",
    @SerializedName("deleted")
    @Expose
    var deleted: Boolean = false,
    @SerializedName("description")
    @Expose
    var description: String = "",
    @SerializedName("id")
    @Expose
    var id: String = "",
    @SerializedName("isActive")
    @Expose
    var isActive: Boolean = false,
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("program")
    @Expose
    var program: Program = Program(),
    @SerializedName("programId")
    @Expose
    var programId: String = ""
)