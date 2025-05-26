package com.lawyer_archives.models

data class CourtSession(
    var id: String = "",
    var caseId: String = "",
    var caseTitle: String = "",
    var sessionDate: String = "",
    var location: String = "",
    var description: String = "",
    var isCompleted: Boolean = false
)