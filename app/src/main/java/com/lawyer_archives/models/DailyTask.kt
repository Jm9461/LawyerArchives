package com.lawyer_archives.models

data class DailyTask(
    var id: String = "",
    var userId: String = "", 
    var title: String = "",
    var description: String = "",
    var dueDate: String = "",
    var dueTime: String = "",
    var priority: String = "",
    var isCompleted: Boolean = false,
    var addedDate: String = "",
    val relatedClientOrCase: String? = null
)