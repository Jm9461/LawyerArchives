package com.lawyer_archives.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddLegalClientBinding
import com.lawyer_archives.models.LegalClient
import com.lawyer_archives.utils.XmlManager
import saman.zamani.persiandate.PersianDate
import java.util.*

class AddLegalClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLegalClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLegalClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSaveButton()
    }

    private fun setupSaveButton() {
        binding.btnSaveLegalClient.setOnClickListener {
            saveLegalClient()
        }
    }

    private fun saveLegalClient() {
        val companyName = binding.etCompanyName.text.toString().trim()
        val nationalId = binding.etLegalNationalId.text.toString().trim()
        val registrationNumber = binding.etRegistrationNumber.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val managerName = binding.etLegalRepresentativeName.text.toString().trim()
        val phoneNumber = binding.etMobile.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val economicCode = binding.etEconomicCode.text.toString().trim()
        val postalCode = binding.etPostalCode.text.toString().trim()
        val representativeNationalId = binding.etRepresentativeNationalId.text.toString().trim()

        if (companyName.isEmpty() || nationalId.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "لطفاً فیلدهای اجباری (نام شرکت، شناسه ملی، آدرس، تلفن) را پر کنید.", Toast.LENGTH_LONG).show()
            return
        }

        // دریافت تاریخ شمسی فعلی
        val persianDate = PersianDate()
        val currentDateString = String.format(
            Locale.getDefault(),
            "%d/%02d/%02d",
            persianDate.shYear,
            persianDate.shMonth,
            persianDate.shDay
        )

        val legalClient = LegalClient(
            companyName = companyName,
            nationalId = nationalId,
            registrationNumber = registrationNumber,
            phone = phone,
            address = address,
            managerName = managerName,
            phoneNumber = phoneNumber,
            email = email,
            description = description,
            addedDate = currentDateString,
            economicCode = economicCode,
            postalCode = postalCode,
            legalRepresentativeName = managerName,
            representativeNationalId = representativeNationalId
        )

        // بارگذاری موکلین حقوقی موجود و اضافه کردن جدید
        val existingClients = XmlManager.loadLegalClients(this)
        val updatedClients = existingClients.toMutableList().apply {
            add(legalClient)
        }

        val success = XmlManager.saveLegalClients(this, updatedClients)
        if (success) {
            Toast.makeText(this, "موکل حقوقی با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "خطا در ذخیره موکل حقوقی", Toast.LENGTH_SHORT).show()
        }
    }
}