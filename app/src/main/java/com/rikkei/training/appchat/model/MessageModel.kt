package com.rikkei.training.appchat.model

data class MessageModel(
    var content: String? = null,
    var senderId: String? = null,
    var time: String? = null,
    var imgUrl: String? = null
)
