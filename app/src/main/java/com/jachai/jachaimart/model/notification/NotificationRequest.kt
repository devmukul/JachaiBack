package bd.com.evaly.ehealth.models.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationRequest {
    @Expose
    @SerializedName("notification_id")
    var notificationId: String? = null

    @Expose
    @SerializedName("mark_as_read")
    var markAllAsRead: String? = null

    @SerializedName("notification_body")
    @Expose
    var notificationBody: String? = null

    @SerializedName("notification_title")
    @Expose
    var notificationTitle: String? = null

    @SerializedName("notification_type")
    @Expose
    var notificationType: String? = null

    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null

    @SerializedName("invoice_no")
    @Expose
    private var invoiceNo: String? = null

    @SerializedName("to")
    @Expose
    var to: String? = null

    @SerializedName("notification_topic")
    @Expose
    var notificationTopic: String? = null
    fun getInvoiceNo(): Any? {
        return invoiceNo
    }

    fun setInvoiceNo(invoiceNo: String?) {
        this.invoiceNo = invoiceNo
    }
}