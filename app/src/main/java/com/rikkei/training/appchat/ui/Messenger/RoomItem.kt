package com.rikkei.training.appchat.ui.Messenger

import com.rikkei.training.appchat.model.Room
import com.rikkei.training.appchat.model.UsersModel

interface RoomItem {
    fun getDetail(user: UsersModel)

    fun room(room: Room)
}