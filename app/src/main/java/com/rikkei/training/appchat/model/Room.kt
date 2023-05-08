package com.rikkei.training.appchat.model

data class Room(
    var name: String? = null,
    var image: String? = null,
    var unreadMessenger: Int = 0,
    var lastMessenger: String? = null,
    var timeStamp: String? = null
)
