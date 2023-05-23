package com.rikkei.training.appchat.ui.message

data class MessageRecyclerViewModel(
    val message: MessageModel,
    var messageType: Int,
    val profileUrl: String
)