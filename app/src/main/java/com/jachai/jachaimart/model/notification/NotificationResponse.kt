package bd.com.evaly.ehealth.models.notification

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationResponse : Parcelable {
    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("recipient_user")
    @Expose
    var recipientUser: String? = null

    @SerializedName("notification_type")
    @Expose
    var notificationType: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("resource_id")
    @Expose
    var resourceId: String? = null

    @SerializedName("timestamp")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    var v: Int? = null

    @SerializedName("unread")
    @Expose
    var unReadCount: String? = null

    @SerializedName("mark_as_read")
    @Expose
    var markAsRead: Boolean? = null

    @SerializedName("icon")
    @Expose
    var imageUrl: String? = null

    @SerializedName("redirect_url")
    @Expose
    var redirectUrl: String? = null

    constructor()
    protected constructor(`in`: Parcel) {
        type = `in`.readString()
        status = `in`.readString()
        id = `in`.readString()
        recipientUser = `in`.readString()
        notificationType = `in`.readString()
        title = `in`.readString()
        body = `in`.readString()
        resourceId = `in`.readString()
        createdAt = `in`.readString()
        updatedAt = `in`.readString()
        v = if (`in`.readByte().toInt() == 0) {
            null
        } else {
            `in`.readInt()
        }
        unReadCount = `in`.readString()
        val tmpMarkAsRead = `in`.readByte()
        markAsRead = if (tmpMarkAsRead.toInt() == 0) null else tmpMarkAsRead.toInt() == 1
        imageUrl = `in`.readString()
        redirectUrl = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(type)
        dest.writeString(status)
        dest.writeString(id)
        dest.writeString(recipientUser)
        dest.writeString(notificationType)
        dest.writeString(title)
        dest.writeString(body)
        dest.writeString(resourceId)
        dest.writeString(createdAt)
        dest.writeString(updatedAt)
        if (v == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(v!!)
        }
        dest.writeString(unReadCount)
        dest.writeByte((if (markAsRead == null) 0 else if (markAsRead as Boolean) 1 else 2).toByte())
        dest.writeString(imageUrl)
        dest.writeString(redirectUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<NotificationResponse?> = object : Parcelable.Creator<NotificationResponse?> {
            override fun createFromParcel(`in`: Parcel): NotificationResponse? {
                return NotificationResponse(`in`)
            }

            override fun newArray(size: Int): Array<NotificationResponse?> {
                return arrayOfNulls(size)
            }
        }
    }
}