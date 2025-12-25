package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.ClientAdapter
import com.lawyer_archives.databinding.ActivityClientsBinding
import com.lawyer_archives.models.ClientEntry
import com.lawyer_archives.models.LegalClient
import com.lawyer_archives.models.RealClient
import com.lawyer_archives.utils.XmlManager

class ClientsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientsBinding
    private lateinit var adapter: ClientAdapter
    private var clientList: MutableList<ClientEntry> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadClients()

        binding.fabAddClient.setOnClickListener {
            startActivity(Intent(this, AddClientActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadClients()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewClients.layoutManager = LinearLayoutManager(this)

        // درست شده: دیگر context پاس داده نمی‌شود چون آداپتور دیگر نمی‌خواهد
        adapter = ClientAdapter(
            onEditClick = { client ->
                val intent = Intent(this@ClientsActivity, EditClientActivity::class.java).apply {
                    putExtra("clientId", client.id)
                }
                startActivity(intent)
            },
            onDeleteClick = { client ->
                confirmAndDeleteClient(client)
            }
        )

        binding.recyclerViewClients.adapter = adapter
    }

    private fun loadClients() {
        val realClients = XmlManager.loadRealClients(this)
        val legalClients = XmlManager.loadLegalClients(this)

        clientList.clear()
        clientList.addAll(realClients)
        clientList.addAll(legalClients)

        adapter.updateList(clientList)

        if (clientList.isEmpty()) {
            Toast.makeText(this, "هنوز موکلی ثبت نشده است.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmAndDeleteClient(client: ClientEntry) {
        val name = when (client) {
            is RealClient -> client.fullName
            is LegalClient -> client.companyName
            else -> "موکل"
        }

        AlertDialog.Builder(this)
            .setTitle("حذف موکل")
            .setMessage("آیا از حذف موکل \"$name\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ -> deleteClient(client) }
            .setNegativeButton("خیر", null)
            .show()
    }

    private fun deleteClient(client: ClientEntry) {
        clientList.remove(client)

        when (client) {
            is RealClient -> XmlManager.saveRealClients(this, clientList.filterIsInstance<RealClient>())
            is LegalClient -> XmlManager.saveLegalClients(this, clientList.filterIsInstance<LegalClient>())
        }

        adapter.updateList(clientList)
        Toast.makeText(this, "موکل با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
    }
}