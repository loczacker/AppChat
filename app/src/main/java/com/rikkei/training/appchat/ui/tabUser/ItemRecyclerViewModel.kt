package com.rikkei.training.appchat.ui.tabUser

import com.rikkei.training.appchat.model.UsersModel

data class ItemRecyclerViewModel(

    var user: UsersModel,
    var statusButton: String = "not_friend"
)