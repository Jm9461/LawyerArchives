package com.lawyer_archives.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.util.Log
import androidx.core.content.ContextCompat

class SessionReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val phoneNumber = intent.getStringExtra("PHONE_NUMBER")
        val message = intent.getStringExtra("MESSAGE")

        if (phoneNumber != null && message != null) {
            // Check for SMS permission before sending
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    val smsManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        // برای API 31 و بالاتر از روش جدید استفاده می‌کنیم
                        context.getSystemService(SmsManager::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        SmsManager.getDefault()
                    }
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                    Log.d("SessionReminder", "SMS sent successfully to $phoneNumber")
                } catch (e: SecurityException) {
                    // Handle SecurityException - permission might be revoked at runtime
                    Log.e("SessionReminder", "SecurityException: SMS permission denied", e)
                } catch (e: Exception) {
                    // Handle other potential exceptions
                    Log.e("SessionReminder", "Failed to send SMS: ${e.message}", e)
                }
            } else {
                // Permission not granted, log the event
                Log.w("SessionReminder", "SEND_SMS permission not granted. Cannot send reminder SMS.")
                
                // Optional: You might want to schedule a retry or notify the user
                // Note: In a BroadcastReceiver, avoid starting activities directly
                // as it might not provide good user experience
            }
        } else {
            Log.w("SessionReminder", "Missing phone number or message in intent extras")
        }
    }
}