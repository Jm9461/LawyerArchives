package com.lawyer_archives.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.activities.AddClientActivity
import com.lawyer_archives.activities.EditRealClientActivity
import com.lawyer_archives.activities.EditLegalClientActivity
import com.lawyer_archives.adapters.ClientAdapter
import com.lawyer_archives.databinding.FragmentClientsBinding
import com.lawyer_archives.models.ClientEntry
import com.lawyer_archives.models.LegalClient
import com.lawyer_archives.models.RealClient
import com.lawyer_archives.utils.XmlManager

class ClientsFragment : Fragment() {

    private var _binding: FragmentClientsBinding? = null
    private val binding get() = _binding!!
    private lateinit var clientAdapter: ClientAdapter
    private var allClients = mutableListOf<ClientEntry>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        loadClients()
    }

    private fun setupRecyclerView() {
    clientAdapter = ClientAdapter(
        onEditClick = { client ->
            when (client) {
                is RealClient -> {
                    val intent = Intent(requireContext(), EditRealClientActivity::class.java).apply {
                        putExtra("clientId", client.id)
                    }
                    startActivity(intent)
                }
                is LegalClient -> {
                    val intent = Intent(requireContext(), EditLegalClientActivity::class.java).apply {
                        putExtra("clientId", client.id)
                    }
                    startActivity(intent)
                }
            }
        },
        onDeleteClick = { client ->
            confirmAndDeleteClient(client)
        }
    )
    
    binding.recyclerViewClients.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = clientAdapter
    }
}

    private fun setupClickListeners() {
        binding.fabAddClient.setOnClickListener {
            startActivity(Intent(requireContext(), AddClientActivity::class.java))
        }
    }

    private fun loadClients() {
        val realClients = XmlManager.loadRealClients(requireContext())
        val legalClients = XmlManager.loadLegalClients(requireContext())

        allClients.clear()
        allClients.addAll(realClients)
        allClients.addAll(legalClients)

    clientAdapter.updateList(allClients.toList())  // درست: updateList نه submitList

    if (allClients.isEmpty()) {
        Toast.makeText(requireContext(), "هنوز موکلی ثبت نشده است.", Toast.LENGTH_SHORT).show()
    }
}

    private fun confirmAndDeleteClient(client: ClientEntry) {
        val clientName = when (client) {
            is RealClient -> client.fullName
            is LegalClient -> client.companyName
            else -> "این موکل"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("حذف موکل")
            .setMessage("آیا از حذف موکل \"$clientName\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteClient(client)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    private fun deleteClient(client: ClientEntry) {
        when (client) {
            is RealClient -> {
                val realClients = XmlManager.loadRealClients(requireContext()).toMutableList()
                realClients.removeAll { it.id == client.id }
                XmlManager.saveRealClients(requireContext(), realClients)
                Toast.makeText(requireContext(), "موکل حقیقی با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
            }
            is LegalClient -> {
                val legalClients = XmlManager.loadLegalClients(requireContext()).toMutableList()
                legalClients.removeAll { it.id == client.id }
                XmlManager.saveLegalClients(requireContext(), legalClients)
                Toast.makeText(requireContext(), "موکل حقوقی با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
            }
        }
        loadClients()
    }

    override fun onResume() {
        super.onResume()
        loadClients()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}