package com.lawyer_archives.models

import java.util.UUID

data class Client(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String = "",
    override val phoneNumber: String = "",
    override val addedDate: String = "",
    val email: String = "",
    val type: String = "", // "real" or "legal"
    val nationalCode: String = "",
    val job: String = "",
    val mobilePhone: String = "",
    val landline: String = "",
    val homeAddress: String = "",
    val workAddress: String = ""
) : ClientEntry