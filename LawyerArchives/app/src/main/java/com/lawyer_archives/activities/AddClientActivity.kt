package com.lawyer_archives.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddClientBinding
import com.lawyer_archives.models.Client
import com.lawyer_archives.utils.XmlManager
import java.util.*

class AddClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveClientButton.setOnClickListener {
            val name = binding.editClientName.text.toString()
            val phone = binding.editClientPhone.text.toString()
            val email = binding.editClientEmail.text.toString()
            val address = binding.editClientAddress.text.toString()

            val client = Client(
                id = UUID.randomUUID().toString(),
                name = name,
                phone = phone,
                email = email,
                address = address
            )

            val currentClients = XmlManager.loadClients(this)
            currentClients.add(client)
            XmlManager.saveClients(this, currentClients)

            finish()
        }
    }
}