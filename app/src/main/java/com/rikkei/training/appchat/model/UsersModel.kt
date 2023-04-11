package com.rikkei.training.appchat.model

data class UsersModel(
    var birthday: String ?= null,
    var email: String? = null,
    var img: String ?= null,
    var name: String? = null,
    var phone: String ?= null,
    var uid: String ?= null,
    var presence: String ?= null
)
