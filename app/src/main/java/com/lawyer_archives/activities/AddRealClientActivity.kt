package com.lawyer_archives.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddRealClientBinding
import com.lawyer_archives.models.RealClient
import com.lawyer_archives.utils.XmlManager
import saman.zamani.persiandate.PersianDate
import java.util.*

class AddRealClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRealClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRealClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveRealClient.setOnClickListener {
            saveRealClient()
        }
    }

    private fun saveRealClient() {
        val fullName = binding.etFullName.text.toString().trim()
        val fatherName = binding.etFatherName.text.toString().trim()
        val idCardNumber = binding.etIdCardNumber.text.toString().trim()
        val nationalId = binding.etNationalId.text.toString().trim()
        val birthDate = binding.etBirthDate.text.toString().trim()
        val birthPlace = binding.etBirthPlace.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val occupation = binding.etOccupation.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val postalCode = binding.etPostalCode.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        if (fullName.isEmpty() || nationalId.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "نام کامل، کد ملی و تلفن ثابت الزامی است", Toast.LENGTH_SHORT).show()
            return
        }

        val persianDate = PersianDate()
        val addedDate = String.format(
            "%04d/%02d/%02d",
            persianDate.shYear,
            persianDate.shMonth,
            persianDate.shDay
        )

        val newClient = RealClient(
            fullName = fullName,
            fatherName = fatherName,
            idCardNumber = idCardNumber,
            nationalId = nationalId,
            birthDate = birthDate,
            birthPlace = birthPlace,
            address = address,
            phone = phone,
            phoneNumber = mobile,
            occupation = occupation,
            email = email,
            postalCode = postalCode,
            description = description,
            addedDate = addedDate
        )

        val list = XmlManager.loadRealClients(this).toMutableList()
        list.add(newClient)

        if (XmlManager.saveRealClients(this, list)) {
            Toast.makeText(this, "موکل حقیقی با موفقیت اضافه شد", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "خطا در ذخیره سازی", Toast.LENGTH_SHORT).show()
        }
    }
}