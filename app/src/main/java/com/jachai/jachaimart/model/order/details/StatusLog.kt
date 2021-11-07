package com.jachai.jachaimart.model.order.details


import com.google.gson.annotations.SerializedName

data class StatusLog(
    @SerializedName("createdBy")
    var createdBy: CreatedBy,
    @SerializedName("datetime")
    var datetime: String,
    @SerializedName("id")
    var id: Any,
    @SerializedName("note")
    var note: String,
    @SerializedName("value")
    var value: String
)