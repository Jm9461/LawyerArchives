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

        val originalClient = intent.getParcelableExtra<Client>("client")!!
        clientList = XmlManager.loadClients(this)

        binding.editClientName.setText(originalClient.name)
        binding.editClientPhone.setText(originalClient.phone)
        binding.editClientEmail.setText(originalClient.email)
        binding.editClientAddress.setText(originalClient.address)

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