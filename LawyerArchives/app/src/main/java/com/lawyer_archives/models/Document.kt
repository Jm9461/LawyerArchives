// LawyerArchives/app/src/main/java/com/lawyer_archives/models/Document.kt
package com.lawyer_archives.models

data class Document(
    var id: String = "",
    var caseId: String = "", // ID پرونده‌ای که این سند به آن تعلق دارد
    var title: String = "", // عنوان سند (مثلاً: "دادنامه پرونده 123")
    var filePath: String = "", // مسیر URI سند در حافظه
    var fileExtension: String = "", // پسوند فایل (مثلاً: "pdf", "jpg", "docx")
    var addedDate: String = "" // تاریخ افزودن سند
)