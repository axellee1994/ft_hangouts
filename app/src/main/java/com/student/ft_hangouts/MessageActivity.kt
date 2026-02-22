package com.student.ft_hangouts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageActivity : BaseActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var lvMessages: ListView
    private lateinit var etMessageInput: EditText
    private lateinit var btnSend: Button

    private var contactId: Long = -1
    private var contactPhone: String = ""

    companion object {
        private const val SMS_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        databaseHelper = DatabaseHelper(this)
        
        lvMessages = findViewById(R.id.lvMessages)
        etMessageInput = findViewById(R.id.etMessageInput)
        btnSend = findViewById(R.id.btnSend)

        // Retrieve data passed from ViewContactActivity
        contactId = intent.getLongExtra("CONTACT_ID", -1)
        contactPhone = intent.getStringExtra("CONTACT_PHONE") ?: ""

        if (contactId == -1L || contactPhone.isEmpty()) {
            Toast.makeText(this, "Error loading chat", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set the header title to the contact's phone number
        supportActionBar?.title = contactPhone

        // Load existing messages
        loadMessages()

        btnSend.setOnClickListener {
            val messageText = etMessageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                checkPermissionAndSendMessage(messageText)
            }
        }
    }

    private fun loadMessages() {
        // We will build a custom adapter for this list in the next step, 
        // but for now, we just want to retrieve the data
        val messages = databaseHelper.getMessagesForContact(contactId)
        // TODO: Bind messages to ListView
    }

    private fun checkPermissionAndSendMessage(messageText: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sendMessage(messageText)
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_CODE)
        }
    }

    private fun sendMessage(messageText: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(contactPhone, null, messageText, null, null)

            // Save message to database
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val timestamp = sdf.format(Date())

            val newMessage = Message(
                contactId = contactId,
                text = messageText,
                timestamp = timestamp,
                isSent = true
            )
            
            databaseHelper.addMessage(newMessage)
            
            // Clear input field and reload messages
            etMessageInput.text.clear()
            loadMessages()
            
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
            
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, trigger send again
                val messageText = etMessageInput.text.toString().trim()
                if (messageText.isNotEmpty()) {
                    sendMessage(messageText)
                }
            } else {
                Toast.makeText(this, "SMS Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}