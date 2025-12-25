package com.lawyer_archives.models

data class GeneralMeeting(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val description: String,
    val addedDate: String
)