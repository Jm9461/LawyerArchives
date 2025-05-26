package com.lawyer_archives.models

data class Meeting(
    var id: String = "",
    var clientId: String = "",
    var clientName: String = "",
    var meetingDate: String = "",
    var topic: String = "",
    var duration: String = "",
    var location: String = ""
)