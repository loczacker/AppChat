package com.rikkei.training.appchat.ui.tabUser

import com.rikkei.training.appchat.model.UsersModel

data class ItemRecyclerViewModel(
    val user: UsersModel,
    var statusButton: String = "not_friend"
)