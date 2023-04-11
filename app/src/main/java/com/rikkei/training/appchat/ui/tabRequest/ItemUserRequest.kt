package com.rikkei.training.appchat.ui.tabRequest

import com.rikkei.training.appchat.model.UsersModel

interface ItemUserRequest {

    fun getRequest(user: UsersModel)
}