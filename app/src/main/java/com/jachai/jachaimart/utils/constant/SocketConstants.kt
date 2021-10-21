package com.jachai.jachaimart.utils.constant

object SocketConstants {


    //Received Destination
    const val RIDER_LOCATION_RECEIVER = "/user/queue/driver"
    const val TRIP_REQUEST_RECEIVER = "/user/queue/trip"


    //Send Destination
    const val RIDER_LOCATION_SEND = "/app/location"
    const val RIDER_TRIP_CANCEL_SEND = "/app/call/cancel"
    const val RIDER_TRIP_ACCEPT_SEND = "/app/call/accept"
    const val RIDER_TRIP_PICKED_SEND = "/app/call/picked"
    const val RIDER_TRIP_DROP_OFF_SEND = "/app/call/drop-off"
    const val RIDER_TRIP_COMPLETED_SEND = "/app/call/completed"






    var CURRENT_FRAGMENT = "na"


}