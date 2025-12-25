package com.lawyer_archives.activities

import android.Manifest
import android.app.Activity
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
    private lateinit var adapter: DocumentAdapter
    private var caseId: String = ""
    private var caseTitle: String? = null

    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            uri?.let {
                copyFileToInternalStorage(it, caseId)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
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

        caseId = intent.getStringExtra("CASE_ID") ?: ""
        caseTitle = intent.getStringExtra("caseTitle")

        binding.documentsHeader.text = caseTitle ?: "اسناد پرونده"

        setupRecyclerView()
        loadDocumentsForCase()

        binding.addDocumentButton.setOnClickListener {
            checkPermissionsAndOpenFilePicker()
        }
    }

    private fun setupRecyclerView() {
        binding.documentsRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DocumentAdapter(
    onOpenClick = { document ->
        openDocument(document)
    },
    onDeleteClick = { document ->
        confirmAndDeleteDocument(document)
    }
)
        
        binding.documentsRecyclerView.adapter = adapter
    }

    private fun loadDocumentsForCase() {
        val allDocuments = XmlManager.loadDocuments(this)
        val caseDocuments = allDocuments.filter { it.caseId == caseId || it.relatedCaseId == caseId }
        
        if (caseDocuments.isEmpty()) {
            binding.emptyListMessage.visibility = android.view.View.VISIBLE
        } else {
            binding.emptyListMessage.visibility = android.view.View.GONE
        }
        adapter.submitList(caseDocuments)
    }

    private fun checkPermissionsAndOpenFilePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            openFilePicker()
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openFilePicker()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            val mimeTypes = arrayOf(
                "application/pdf",
                "image/jpeg",
                "image/png",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            )
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        pickFileLauncher.launch(intent)
    }

    private fun copyFileToInternalStorage(uri: Uri, caseId: String) {
        val fileName = getFileName(uri)
        val fileExtension = getFileExtension(fileName)
        val mimeType = contentResolver.getType(uri) ?: getMimeType(fileExtension)
        val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
        val destinationFile = File(filesDir, uniqueFileName)

        try {
            contentResolver.openInputStream(uri)?.use { inputStream: InputStream ->
                FileOutputStream(destinationFile).use { outputStream: FileOutputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            
            val newDocument = Document(
                name = fileName,
                filePath = destinationFile.absolutePath,
                mimeType = mimeType,
                relatedCaseId = caseId,
                fileExtension = fileExtension,
                addedDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date()),
                title = fileName,
                caseId = caseId
            )
            
            if (XmlManager.addDocument(this, newDocument)) {
                Toast.makeText(this, "فایل با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show()
                loadDocumentsForCase()
            } else {
                Toast.makeText(this, "خطا در ذخیره اطلاعات سند.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "خطا در ذخیره فایل: ${e.message}", Toast.LENGTH_LONG).show()
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
        return result ?: uri.lastPathSegment ?: "unknown_file"
    }

    private fun getFileExtension(fileName: String): String {
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex != -1 && dotIndex < fileName.length - 1) {
            fileName.substring(dotIndex + 1).lowercase(Locale.ROOT)
        } else {
            ""
        }
    }

    private fun openDocument(document: Document) {
        val file = File(document.filePath)
        if (file.exists()) {
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(Uri.fromFile(file), document.mimeType)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "برنامه‌ای برای باز کردن این فایل یافت نشد.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "فایل پیدا نشد. ممکن است حذف شده باشد.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMimeType(fileExtension: String): String {
        return when (fileExtension.lowercase(Locale.ROOT)) {
            "pdf" -> "application/pdf"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            else -> "application/octet-stream"
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
        if (XmlManager.deleteDocument(this, document.id)) {
            Toast.makeText(this, "سند با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
            loadDocumentsForCase()
        } else {
            Toast.makeText(this, "خطا در حذف سند.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadDocumentsForCase()
    }
}