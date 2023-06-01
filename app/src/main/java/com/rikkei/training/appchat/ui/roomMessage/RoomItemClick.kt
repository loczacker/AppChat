package com.rikkei.training.appchat.ui.roomMessage

import com.rikkei.training.appchat.model.RoomModel

interface RoomItemClick {
    fun getRoomInfo(roomModel: RoomModel)
}