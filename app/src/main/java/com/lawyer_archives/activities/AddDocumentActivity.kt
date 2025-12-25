package com.lawyer_archives.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddDocumentBinding
import com.lawyer_archives.models.Document
import com.lawyer_archives.utils.XmlManager
import java.text.SimpleDateFormat
import java.util.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher

class AddDocumentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDocumentBinding
    private var selectedFileUri: Uri? = null
    private lateinit var caseId: String
    private lateinit var filePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        caseId = intent.getStringExtra("caseId") ?: ""

        filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedFileUri = uri
                    val fileName = getFileName(uri)
                    binding.selectedFileName.text = fileName
                }
            }
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.selectFileButton.setOnClickListener {
            openFilePicker()
        }

        binding.saveButton.setOnClickListener {
            if (validateInputs()) {
                saveDocument()
            }
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        filePickerLauncher.launch(intent)
    }

    private fun getFileName(uri: Uri): String {
        var result = ""
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    result = cursor.getString(displayNameIndex)
                }
            }
        }
        return result.ifEmpty { "نامشخص" }
    }

    private fun validateInputs(): Boolean {
        if (binding.documentTitle.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "لطفاً عنوان سند را وارد کنید", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedFileUri == null) {
            Toast.makeText(this, "لطفاً یک فایل انتخاب کنید", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveDocument() {
        try {
            val title = binding.documentTitle.text.toString().trim()
            val fileUri = selectedFileUri!!
            
            val document = Document(
                id = UUID.randomUUID().toString(),
                title = title,
                filePath = fileUri.toString(),
                mimeType = contentResolver.getType(fileUri) ?: "",
                relatedCaseId = caseId,
                fileExtension = getFileExtension(fileUri),
                addedDate = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date()),
                caseId = caseId,
                name = getFileName(fileUri)
            )

            if (XmlManager.addDocument(this, document)) {
                Toast.makeText(this, "سند با موفقیت افزوده شد", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "خطا در ذخیره سند", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "خطا: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExtension(uri: Uri): String {
        val fileName = getFileName(uri)
        return if (fileName.contains(".")) {
            fileName.substringAfterLast(".", "")
        } else {
            ""
        }
    }
}