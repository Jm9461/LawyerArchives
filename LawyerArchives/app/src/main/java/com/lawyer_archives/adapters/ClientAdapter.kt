package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.databinding.ItemClientBinding
import com.lawyer_archives.models.Client

class ClientAdapter(
    private val onEditClick: (Client) -> Unit,
    private val onDeleteClick: (Client) -> Unit
) : ListAdapter<Client, ClientAdapter.ClientViewHolder>(ClientDiffCallback()) {

    inner class ClientViewHolder(private val binding: ItemClientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Client) {
            binding.clientName.text = item.name
            binding.clientPhone.text = "تلفن: ${item.phone}"
            binding.clientEmail.text = "ایمیل: ${item.email}"

            binding.editButton.setOnClickListener {
                onEditClick(item)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemClientBinding.inflate(inflater, parent, false)
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ClientDiffCallback : DiffUtil.ItemCallback<Client>() {
        override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean =
            oldItem == newItem
    }
}