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
            val uri = result.data?.data
            uri?.let {
                saveDocumentFromUri(it)
            } ?: Toast.makeText(this, "فایل انتخاب نشد.", Toast.LENGTH_SHORT).show()
        }
    }

    // Activity Result Launcher for requesting permissions
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            openFilePicker()
        } else {
            Toast.makeText(this, "مجوز دسترسی به حافظه برای افزودن سند لازم است.", Toast.LENGTH_LONG).show()
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
            checkPermissionsAndOpenFilePicker()
        }
    }

    private fun setupRecyclerView() {
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
        documentList = XmlManager.loadDocuments(this)
            .filter { it.caseId == caseId }
            .toMutableList()
        adapter.submitList(documentList)
        if (documentList.isEmpty()) {
            Toast.makeText(this, "هنوز سندی برای این پرونده ثبت نشده است.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissionsAndOpenFilePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            // For Android 13 and above, READ_EXTERNAL_STORAGE is deprecated for media files.
            // We use Media Visual Document Picker, which doesn't require explicit permission
            // for picking files. So, no explicit permission check for new APIs.
            openFilePicker()
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            openFilePicker()
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*" // Allow all file types
            // Specify supported MIME types
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                "application/pdf",
                "image/jpeg",
                "image/png",
                "application/msword", // .doc
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document" // .docx
            ))
        }
        pickFileLauncher.launch(intent)
    }

    private fun saveDocumentFromUri(uri: Uri) {
        // First, get the file name and extension from the URI
        var fileName: String? = null
        var fileExtension: String = ""
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = cursor.getString(nameIndex)
                fileExtension = fileName?.substringAfterLast('.', "")?.lowercase(Locale.ROOT) ?: ""
            }
        }

        if (fileName == null || fileName!!.isEmpty()) {
            Toast.makeText(this, "نام فایل قابل تشخیص نیست.", Toast.LENGTH_SHORT).show()
            return
        }

        // Generate a unique ID for the document
        val documentId = UUID.randomUUID().toString()
        val uniqueFileName = "$documentId.$fileExtension"

        try {
            // Open an input stream from the URI
            val inputStream: InputStream? = contentResolver.openInputStream(uri)

            // Define the destination file in the app's internal storage
            val outputDir = File(filesDir, "documents") // Create a subdirectory for documents
            if (!outputDir.exists()) {
                outputDir.mkdirs() // Create the directory if it doesn't exist
            }
            val destinationFile = File(outputDir, uniqueFileName)

            // Copy the file from URI to internal storage
            inputStream?.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }

            // Create a new Document object
            val newDocument = Document(
                id = documentId,
                caseId = caseId ?: "",
                title = fileName.substringBeforeLast("."), // Use original file name as title
                filePath = destinationFile.absolutePath, // Store the internal path
                fileExtension = fileExtension,
                addedDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
            )

            documentList.add(newDocument)
            XmlManager.saveDocuments(this, documentList)
            adapter.submitList(documentList.toList()) // Update RecyclerView
            Toast.makeText(this, "سند با موفقیت اضافه شد.", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "خطا در ذخیره سند: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openDocument(document: Document) {
        val file = File(document.filePath)
        if (file.exists()) {
            val uri = Uri.fromFile(file)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, getMimeType(document.fileExtension))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission to external app
            }
            // Check if there's an app to handle the intent
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "برنامه‌ای برای باز کردن این نوع فایل یافت نشد.", Toast.LENGTH_SHORT).show()
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
        XmlManager.saveDocuments(this, documentList)
        adapter.submitList(documentList.toList())
        Toast.makeText(this, "سند از لیست حذف شد.", Toast.LENGTH_SHORT).show()
    }
}