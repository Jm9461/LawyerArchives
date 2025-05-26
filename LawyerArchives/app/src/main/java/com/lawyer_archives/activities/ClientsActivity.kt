package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.ClientAdapter
import com.lawyer_archives.databinding.ActivityClientsBinding
import com.lawyer_archives.models.Client
import com.lawyer_archives.utils.XmlManager

class ClientsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientsBinding
    private lateinit var adapter: ClientAdapter
    private lateinit var clientList: MutableList<Client>

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

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ClientAdapter(
            onEditClick = { client ->
                val intent = Intent(this, EditClientActivity::class.java)
                intent.putExtra("client", client)
                startActivity(intent)
            },
            onDeleteClick = { client ->
                deleteClient(client)
            }
        )
        binding.recyclerView.adapter = adapter
    }

    private fun loadClients() {
        clientList = XmlManager.loadClients(this)
        adapter.submitList(clientList)
    }

    private fun deleteClient(client: Client) {
        clientList.remove(client)
        XmlManager.saveClients(this, clientList)
        adapter.submitList(clientList.toList())
    }

    override fun onResume() {
        super.onResume()
        loadClients()
    }
}