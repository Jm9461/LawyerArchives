package com.lawyer_archives.models

data class Case(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var clientName: String = "",
    var courtDate: String = "",
    var status: String = "",
    var addedDate: String = ""
)