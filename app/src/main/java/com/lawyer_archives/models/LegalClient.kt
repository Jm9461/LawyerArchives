package com.lawyer_archives.models

import java.util.UUID

data class LegalClient(
    override val id: String = UUID.randomUUID().toString(),
    val companyName: String,
    val nationalId: String,
    val registrationNumber: String,
    val phone: String,
    val address: String,
    val managerName: String,
    override val phoneNumber: String,
    val email: String,
    val description: String,
    override val addedDate: String,
    val registrationDate: String = "",
    val legalNationalId: String = "",
    val economicCode: String = "",
    val postalCode: String = "",
    val legalRepresentativeName: String = "",
    val representativeNationalId: String = ""
) : ClientEntry {
    override val name: String
        get() = companyName
}