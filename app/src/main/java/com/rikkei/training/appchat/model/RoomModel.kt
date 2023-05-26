package com.rikkei.training.appchat.model

data class RoomModel(
    var unreadMessage: Int = 0,
    var lastMessage: String? = null,
    var timeStamp: String? = null
)
