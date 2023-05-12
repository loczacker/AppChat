package com.rikkei.training.appchat.model

data class MessageRecyclerViewModel(
    val message: MessageModel,
    var messageType: Int,
    val profileUrl: String
)