// LawyerArchives/app/src/main/java/com/lawyer_archives/activities/CaseDocumentsActivity.kt
package com.lawyer_archives.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.DocumentAdapter
import com.lawyer_archives.databinding.ActivityCaseDocumentsBinding
import com.lawyer_archives.models.Document
import com.lawyer_archives.utils.XmlManager
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CaseDocumentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaseDocumentsBinding
    private lateinit var documentList: MutableList<Document>
    private lateinit var adapter: DocumentAdapter
    private var caseId: String? = null
    private var caseTitle: String? = null

    // Activity Result Launcher for picking files
    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                saveDocumentToFile(uri)
            }
        }
    }

    // Activity Result Launcher for requesting permissions
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            openFilePicker()
        } else {
            Toast.makeText(this, "برای افزودن سند، نیاز به مجوز دسترسی به حافظه داریم.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        caseId = intent.getStringExtra("caseId")
        caseTitle = intent.getStringExtra("caseTitle")

        binding.documentsHeader.text = "مدارک پرونده: ${caseTitle ?: "نامشخص"}"

        setupRecyclerView()
        loadDocuments()

        binding.addDocumentButton.setOnClickListener {
            checkPermissionAndOpenFilePicker()
        }
    }

    private fun setupRecyclerView() {
        binding.documentsRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DocumentAdapter(
            onDocumentClick = { document ->
                openDocument(document)
            },
            onDeleteClick = { document ->
                confirmAndDeleteDocument(document)
            }
        )
        binding.documentsRecyclerView.adapter = adapter
    }

    private fun loadDocuments() {
        // Load all documents, then filter by caseId
        val allDocuments = XmlManager.loadDocuments(this)
        documentList = allDocuments.filter { it.caseId == caseId }.toMutableList()
        adapter.submitList(documentList.toList()) // Use toList() to create a new list for DiffUtil
        if (documentList.isEmpty()) {
            Toast.makeText(this, "هنوز سندی برای این پرونده ثبت نشده است.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissionAndOpenFilePicker() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                openFilePicker()
            }
            // For Android 13 (API 33) and above, use media-specific permissions
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // If targeting API 33+, consider using specific media permissions if applicable
                // For general file picking, READ_EXTERNAL_STORAGE is still needed for older APIs.
                // For direct file access on Android 13+, prefer SAF or proper media store APIs.
                // For this example, we'll keep it simple and prompt READ_EXTERNAL_STORAGE for compatibility.
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*" // Allow all file types
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                "application/pdf",
                "image/jpeg",
                "image/png",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            )) // Suggest common document types
        }
        pickFileLauncher.launch(intent)
    }

    private fun saveDocumentToFile(uri: Uri) {
        val fileName = getFileName(uri)
        val fileExtension = getFileExtension(fileName)
        val targetFile = File(filesDir, "$caseId-${UUID.randomUUID()}.$fileExtension") // Unique name per case

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(targetFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            val newDocument = Document(
                id = UUID.randomUUID().toString(),
                caseId = caseId ?: "",
                title = fileName.substringBeforeLast("."), // Remove extension from title
                filePath = targetFile.absolutePath,
                fileExtension = fileExtension,
                addedDate = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date())
            )
            documentList.add(newDocument)
            XmlManager.saveDocuments(this, documentList) // Save the updated list
            adapter.submitList(documentList.toList()) // Update RecyclerView

            Toast.makeText(this, "سند با موفقیت اضافه شد.", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "خطا در ذخیره سند: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1 && cut != null) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "unknown_file"
    }

    private fun getFileExtension(fileName: String): String {
        return fileName.substringAfterLast('.', "").lowercase(Locale.ROOT)
    }

    private fun openDocument(document: Document) {
        val file = File(document.filePath)
        if (file.exists()) {
            val uri = Uri.fromFile(file)
            val mimeType = getMimeType(document.fileExtension)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mimeType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant temporary permission
            }
            try {
                startActivity(Intent.createChooser(intent, "باز کردن سند با..."))
            } catch (e: Exception) {
                Toast.makeText(this, "نرم‌افزار مناسب برای باز کردن این فایل یافت نشد.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "فایل یافت نشد یا حذف شده است.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMimeType(fileExtension: String): String {
        return when (fileExtension.lowercase(Locale.ROOT)) {
            "pdf" -> "application/pdf"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            else -> "*/*"
        }
    }

    private fun confirmAndDeleteDocument(document: Document) {
        AlertDialog.Builder(this)
            .setTitle("حذف سند")
            .setMessage("آیا از حذف سند \"${document.title}\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteDocument(document)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    private fun deleteDocument(document: Document) {
        // Delete the physical file first
        val file = File(document.filePath)
        if (file.exists()) {
            file.delete()
            Toast.makeText(this, "فایل حذف شد.", Toast.LENGTH_SHORT).show()
        }

        // Remove from list and save XML
        documentList.remove(document)
        XmlManager.saveDocuments(this, documentList) // Save the updated list for this case
        adapter.submitList(documentList.toList()) // Update RecyclerView
        Toast.makeText(this, "سند با موفقیت از لیست حذف شد.", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadDocuments() // Refresh the list in case of changes
    }
}