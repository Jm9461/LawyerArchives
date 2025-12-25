package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.databinding.ItemClientBinding
import com.lawyer_archives.models.ClientEntry
import com.lawyer_archives.models.LegalClient
import com.lawyer_archives.models.RealClient

class ClientAdapter(
    private val onEditClick: (ClientEntry) -> Unit,
    private val onDeleteClick: (ClientEntry) -> Unit
) : RecyclerView.Adapter<ClientAdapter.ViewHolder>() {

    private var clients = listOf<ClientEntry>()

    class ViewHolder(val binding: ItemClientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val client = clients[position]

        with(holder.binding) {
            when (client) {
                is RealClient -> {
                    tvClientName.text = client.fullName
                    tvClientType.text = "حقیقی"
                    tvClientContact.text = client.phoneNumber ?: client.phone ?: "بدون تماس"
                }
                is LegalClient -> {
                    tvClientName.text = client.companyName
                    tvClientType.text = "حقوقی"
                    tvClientContact.text = client.phone ?: "بدون تماس"
                }
            }

            btnEditClient.setOnClickListener { onEditClick(client) }
            btnDeleteClient.setOnClickListener { onDeleteClick(client) }
        }
    }

    override fun getItemCount() = clients.size

    fun updateList(newList: List<ClientEntry>) {
        clients = newList
        notifyDataSetChanged()
    }
}