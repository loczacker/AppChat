package com.rikkei.training.appchat.ui.roomMessage

data class RoomModel(
    var unreadMessage: Int = 0,
    var lastMessage: String? = null,
    var timeStamp: String? = null
)
