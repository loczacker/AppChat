package com.rikkei.training.appchat

data class Users(
    val birthday: String ?= null,
    var email : String? = null,
    var img_profile: String ?= null,
    var name: String? = null ,
    var phone_number: String ?= null,
    var uid : String ? = null
)
