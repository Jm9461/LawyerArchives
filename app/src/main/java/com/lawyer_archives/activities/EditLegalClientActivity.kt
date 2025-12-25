package com.lawyer_archives.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityEditLegalClientBinding
import com.lawyer_archives.models.LegalClient
import com.lawyer_archives.utils.XmlManager
import saman.zamani.persiandate.PersianDate
import java.util.*

class EditLegalClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditLegalClientBinding
    private var originalClientId: String? = null
    private var currentLegalClient: LegalClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLegalClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        originalClientId = intent.getStringExtra("clientId")
        if (originalClientId == null) {
            Toast.makeText(this, "شناسه موکل یافت نشد.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadLegalClientData()

        binding.etRegistrationDate.setOnClickListener { showDatePickerDialog() }
        binding.btnSaveLegalClient.setOnClickListener { updateLegalClient() }
    }

    private fun loadLegalClientData() {
        val legalClients = XmlManager.loadLegalClients(this)
        currentLegalClient = legalClients.find { it.id == originalClientId }

        currentLegalClient?.let { client ->
            binding.etCompanyName.setText(client.companyName)
            binding.etRegistrationNumber.setText(client.registrationNumber)
            binding.etRegistrationDate.setText(client.registrationDate)
            binding.etLegalNationalId.setText(client.legalNationalId)
            binding.etEconomicCode.setText(client.economicCode)
            binding.etAddress.setText(client.address)
            binding.etPhone.setText(client.phone)
            binding.etMobile.setText(client.phoneNumber)
            binding.etEmail.setText(client.email)
            binding.etPostalCode.setText(client.postalCode)
            binding.etDescription.setText(client.description)
            binding.etLegalRepresentativeName.setText(client.legalRepresentativeName)
            binding.etRepresentativeNationalId.setText(client.representativeNationalId)
        } ?: run {
            Toast.makeText(this, "موکل حقوقی برای ویرایش یافت نشد.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showDatePickerDialog() {
        val now = PersianDate()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val persianDate = String.format(
                    Locale.getDefault(),
                    "%d/%02d/%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )
                binding.etRegistrationDate.setText(persianDate)
            },
            now.shYear,
            now.shMonth - 1,
            now.shDay
        )
        datePickerDialog.setTitle("انتخاب تاریخ شمسی")
        datePickerDialog.show()
    }

    private fun updateLegalClient() {
        val companyName = binding.etCompanyName.text.toString().trim()
        val registrationNumber = binding.etRegistrationNumber.text.toString().trim()
        val registrationDate = binding.etRegistrationDate.text.toString().trim()
        val legalNationalId = binding.etLegalNationalId.text.toString().trim()
        val economicCode = binding.etEconomicCode.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val postalCode = binding.etPostalCode.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val legalRepresentativeName = binding.etLegalRepresentativeName.text.toString().trim()
        val representativeNationalId = binding.etRepresentativeNationalId.text.toString().trim()

        if (companyName.isEmpty() || legalNationalId.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "لطفاً فیلدهای اجباری (نام شرکت/سازمان، شناسه ملی، آدرس، تلفن) را پر کنید.", Toast.LENGTH_LONG).show()
            return
        }

        currentLegalClient?.let { existingClient ->
            val updatedLegalClient = existingClient.copy(
                companyName = companyName,
                registrationNumber = registrationNumber,
                registrationDate = registrationDate,
                legalNationalId = legalNationalId,
                economicCode = economicCode,
                address = address,
                phone = phone,
                phoneNumber = mobile,
                email = email,
                postalCode = postalCode,
                description = description,
                legalRepresentativeName = legalRepresentativeName,
                representativeNationalId = representativeNationalId
            )

            val legalClients = XmlManager.loadLegalClients(this).toMutableList()
            val index = legalClients.indexOfFirst { it.id == updatedLegalClient.id }
            if (index != -1) {
                legalClients[index] = updatedLegalClient
                XmlManager.saveLegalClients(this, legalClients)
                Toast.makeText(this, "موکل حقوقی با موفقیت ویرایش شد.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "خطا در ویرایش موکل حقوقی: موکل یافت نشد.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}