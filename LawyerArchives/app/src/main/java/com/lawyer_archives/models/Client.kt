package com.lawyer_archives.models

data class Client(
    var id: String = "",
    var name: String = "",
    var phone: String = "",
    var email: String = "",
    var address: String = "",
    var caseCount: Int = 0,
    var lastMeetingDate: String = ""
)