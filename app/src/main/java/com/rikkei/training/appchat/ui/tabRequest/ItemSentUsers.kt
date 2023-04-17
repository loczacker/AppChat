package com.rikkei.training.appchat.ui.tabRequest

import com.rikkei.training.appchat.model.UsersModel

interface ItemSentUsers {

    fun getDetail(user: UsersModel)

    fun getFriends(user: UsersModel)
}