package com.rikkei.training.appchat.model

data class MessengerModel(
    var messengerId: String ?= null,
    var messenger: String? = null,
    var senderId: String ?= null,
    var imgUrl: String? = null
)
