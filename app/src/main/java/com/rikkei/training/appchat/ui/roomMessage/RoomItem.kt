package com.rikkei.training.appchat.ui.roomMessage

import com.rikkei.training.appchat.model.RoomModel
import com.rikkei.training.appchat.model.UsersModel

interface RoomItem {
    fun getRoomInfo(roomModel: RoomModel)
}