package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddClientBinding

class AddClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cardRealClient.setOnClickListener {
            startActivity(Intent(this, AddRealClientActivity::class.java))
            finish()
        }

        binding.cardLegalClient.setOnClickListener {
            startActivity(Intent(this, AddLegalClientActivity::class.java))
            finish()
        }
    }
}