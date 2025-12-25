package com.lawyer_archives.models

import java.util.UUID

data class Case(
    val id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var formationDate: String = "",
    var clientName: String = "",
    var clientRole: String = "",
    var caseSubject: String = "",
    var status: String = "",
    var process: String = "",
    var caseNumber: String = "",
    var archiveNumber: String = "",
    var cityJudiciary: String = "",
    var courtLevelAndType: String = "",
    var opponentInfo: String = "",
    var powerOfAttorneyNumber: String = "",
    val addedDate: String = "",
    var courtDate: String = "",
    var caseType: String = "",
    var courtName: String = "",
    var courtLocation: String = ""
)