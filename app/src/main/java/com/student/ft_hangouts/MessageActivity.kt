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
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter


class MessageActivity : BaseActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var lvMessages: ListView
    private lateinit var etMessageInput: EditText
    private lateinit var btnSend: Button

    private var contactId: Long = -1
    private var contactPhone: String = ""

    private val smsRefreshReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            loadMessages()
        }
    }

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

    override fun onResume() {
        super.onResume()
        // Start listening for the refresh signal
        ContextCompat.registerReceiver(
            this,
            smsRefreshReceiver,
            IntentFilter("com.student.ft_hangouts.REFRESH_SMS"),
            ContextCompat.RECEIVER_EXPORTED)
        // Refresh messages immediately in case some arrived while we were away
        loadMessages()
    }

    override fun onPause() {
        super.onPause()
        // Stop listening when the activity is not visible to prevent memory leaks
        try {
            unregisterReceiver(smsRefreshReceiver)
        } catch (e: Exception) {
            // Receiver might not be registered
        }
    }

    
    private fun loadMessages() {
        val messages = databaseHelper.getMessagesForContact(contactId)

        val adapter = object : android.widget.ArrayAdapter<Message>(this, R.layout.item_message, R.id.tvMessageText, messages) {
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getView(position, convertView, parent)
                val message = getItem(position)

                val container = view.findViewById<android.widget.LinearLayout>(R.id.messageContainer)
                val tvText = view.findViewById<android.widget.TextView>(R.id.tvMessageText)
                val tvTime = view.findViewById<android.widget.TextView>(R.id.tvTimestamp)

                tvText.text = message?.text
                tvTime.text = message?.timestamp

                // Sent messages -> Right alignment, Light Blue background
                if (message?.isSent == true) {
                    container.gravity = android.view.Gravity.END
                    tvText.setBackgroundResource(android.R.drawable.dialog_holo_light_frame) 
                    tvText.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#E3F2FD"))
                } 
                // Received messages -> Left alignment, Light Grey background
                else {
                    container.gravity = android.view.Gravity.START
                    tvText.setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
                    tvText.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#F5F5F5"))
                }

                return view
            }
        }

        lvMessages.adapter = adapter
        
        // Auto-scroll to the bottom so the newest message is visible
        if (messages.isNotEmpty()) {
            lvMessages.setSelection(messages.size - 1)
        }
    }

    private fun checkPermissionAndSendMessage(messageText: String) {
        val permissions = arrayOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
        )

        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isEmpty()) {
            sendMessage(messageText)
        } else {
            ActivityCompat.requestPermissions(this, missingPermissions.toTypedArray(), SMS_PERMISSION_CODE)
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
