package com.rikkei.training.appchat.model

data class RoomModel(
    var nameRoom: String ?= null,
    var imgRoom: String? = null,
    var lastMessage: String? = null,
    var timeStamp: String? = null,
    var uidFriend: String? = null
)
