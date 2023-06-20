package com.rikkei.training.appchat.model

data class ItemUsersRVModel(
    var user: UsersModel,
    var statusButton: String = "not_friend",
    val isSectionHeader: Boolean = false
)