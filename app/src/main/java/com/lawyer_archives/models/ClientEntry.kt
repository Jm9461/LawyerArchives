package com.lawyer_archives.models

import java.util.UUID

// یک رابط برای نمایش ویژگی‌های مشترک برای همه انواع موکلین
interface ClientEntry {
    val id: String
    val name: String
    val phoneNumber: String
    val addedDate: String
}