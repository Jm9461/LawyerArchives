package com.lawyer_archives.models

import java.util.UUID

data class Document(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var filePath: String = "",
    var mimeType: String = "",
    var relatedCaseId: String = "",
    var fileExtension: String = "",
    var addedDate: String = "",
    var title: String = "",
    var caseId: String = ""
)