package com.rikkei.training.appchat.ui.tabFriends

import com.rikkei.training.appchat.model.MessageModel

interface SearchFriendItemClick {
    fun getMessInfo(message: MessageModel)
}