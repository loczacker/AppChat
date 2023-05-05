package com.rikkei.training.appchat.model

data class Room(
    var idRoom: String? = null,
    var name: String? = null,
    var thumbnail: String? = null,
    var idReceiver: String? = null,
    var unreadMessenger: Int = 0,
    var lastMessenger: String? = null,
    var timeStamp: String? = null
)
