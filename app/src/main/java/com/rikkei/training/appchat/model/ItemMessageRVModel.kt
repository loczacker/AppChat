package com.rikkei.training.appchat.model

data class ItemMessageRVModel(
    val message: MessageModel,
    val isMyMessage : Boolean,
    var messageType: Int,
    val profileUrl: String
)

object MessageType{
    const val TEXT = 1
    const val IMAGE = 2
    const val ICON = 3
}