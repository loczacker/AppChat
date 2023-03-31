package com.rikkei.training.appchat


data class Users(
    val userId: String ?= null,
    val name: String ?= null,
    val email: String ?= null,
    val phone: String ?= null,
    val birthday: String ?= null,
    val imageUrl: String ?= null,
    val password: String ?= null
)
