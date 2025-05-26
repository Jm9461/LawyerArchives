package com.lawyer_archives.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddCaseBinding
import com.lawyer_archives.models.Case
import com.lawyer_archives.utils.XmlManager
import android.widget.Toast
import java.util.UUID

class AddCaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveCaseButton.setOnClickListener {
            saveCase()
        }
    }

    private fun saveCase() {
        val title = binding.editTitle.text.toString()
        val description = binding.editDescription.text.toString()
        val clientName = binding.editClientName.text.toString()
        val courtDate = binding.editCourtDate.text.toString()
        val status = binding.editStatus.text.toString()
        val addedDate = binding.editAddedDate.text.toString()

        if (title.isBlank() || description.isBlank() || clientName.isBlank() || courtDate.isBlank() || status.isBlank() || addedDate.isBlank()) {
            Toast.makeText(this, "لطفاً تمام فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
            return
        }

        val newCase = Case(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            clientName = clientName,
            courtDate = courtDate,
            status = status,
            addedDate = addedDate
        )

        val currentCases = XmlManager.loadCases(this).toMutableList()
        currentCases.add(newCase)
        XmlManager.saveCases(this, currentCases)

        Toast.makeText(this, "پرونده با موفقیت اضافه شد.", Toast.LENGTH_SHORT).show()
        finish()
    }
}