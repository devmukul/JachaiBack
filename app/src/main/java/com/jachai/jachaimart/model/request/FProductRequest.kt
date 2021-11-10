package com.jachai.jachaimart.model.request

import com.google.gson.annotations.SerializedName

class FProductRequest {
    @SerializedName("slugs")
    var slugs: List<String?>? = null
}