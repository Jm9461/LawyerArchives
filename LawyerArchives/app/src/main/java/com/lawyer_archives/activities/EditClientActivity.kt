package com.lawyer_archives.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityEditClientBinding
import com.lawyer_archives.models.Client
import com.lawyer_archives.utils.XmlManager

class EditClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditClientBinding
    private lateinit var clientList: MutableList<Client>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve client ID instead of the whole object
        val clientId = intent.getStringExtra("clientId")
        clientList = XmlManager.loadClients(this)

        // Find the client using the ID
        val originalClient = clientList.find { it.id == clientId }

        originalClient?.let {
            binding.editClientName.setText(it.name)
            binding.editClientPhone.setText(it.phone)
            binding.editClientEmail.setText(it.email)
            binding.editClientAddress.setText(it.address)
        } ?: run {
            // Handle case where client is not found, e.g., finish activity or show error
            finish()
            return
        }

        binding.updateClientButton.setOnClickListener {
            val updatedClient = originalClient.copy(
                name = binding.editClientName.text.toString(),
                phone = binding.editClientPhone.text.toString(),
                email = binding.editClientEmail.text.toString(),
                address = binding.editClientAddress.text.toString()
            )

            val index = clientList.indexOfFirst { it.id == originalClient.id }
            if (index != -1) {
                clientList[index] = updatedClient
                XmlManager.saveClients(this, clientList)
            }

            finish()
        }
    }
}