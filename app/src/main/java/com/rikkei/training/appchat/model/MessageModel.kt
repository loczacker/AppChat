package com.rikkei.training.appchat.model

data class MessageModel(
    var imgFriend: String? = null,
    var content: String? = null,
    var senderId: String? = null,
    var time: String? = null,
    var imgUrl: String? = null,
    var imgIcon: String? = null
)
