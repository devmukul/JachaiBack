package bd.com.evaly.ehealth.models.common

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val city: String,
    val region: String,
    val area: String,
    val address: String,
    val status: String,
    val isPrimary: Boolean,
) : Parcelable
