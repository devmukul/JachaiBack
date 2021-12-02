package bd.com.evaly.ehealth.models.notification


class FcmModel(var to: String, var priority: String, var ttl: Long, var notification: NotificationModel, var data: NotificationDataModel)