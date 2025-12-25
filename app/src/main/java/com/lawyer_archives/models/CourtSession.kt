package com.lawyer_archives.models

import java.util.UUID

data class CourtSession(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val clientName: String = "",
    val courtDate: String = "",
    val courtTime: String = "",
    val courtBranch: String = "",
    val status: String = "",
    val addedDate: String = "",
    val caseTitle: String = "",
    val sessionDate: String = "",
    val location: String = "",
    val caseId: String = "",
    val isCompleted: Boolean = false
)