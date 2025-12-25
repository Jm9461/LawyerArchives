package com.lawyer_archives.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityLoginBinding
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // گزینه ثبت نام رو فعال کن
        binding.tvRegister.setOnClickListener {
            Toast.makeText(this, "قابلیت ثبت نام به زودی اضافه می‌شود", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogin.setOnClickListener {
            // برای تست: مستقیم برو به MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            
            
            // کد اصلی لاگین (فعلاً غیرفعال)
            val enteredPassword = binding.etPassword.text.toString().trim()

            if (enteredPassword.isBlank()) {
                Toast.makeText(this, "لطفاً رمز عبور را وارد کنید.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val storedPasswordHash = sharedPref.getString("password_hash", null)

            if (storedPasswordHash == null) {
                // اگر پسوردی وجود نداره، برو به Setup
                val intent = Intent(this, SetupProfileActivity::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }

            val enteredPasswordHash = hashPassword(enteredPassword)

            if (enteredPasswordHash == storedPasswordHash) {
                Toast.makeText(this, "ورود موفقیت‌آمیز بود.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "رمز عبور اشتباه است.", Toast.LENGTH_SHORT).show()
            }
            
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}