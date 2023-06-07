package com.rikkei.training.appchat.model

data class RoomModel(
    var nameRoom: String ?= null,
    var imgRoom: String? = null,
    var lastMessage: String? = null,
    var timeStamp: String? = null,
    var uidFriend: String? = null,
    var contentMess: String? = null,
    var senderId : String? = null,
    var typeRoomMess: Boolean
) {
    constructor() : this(null, null, null, null, null, null, null, false)
}
