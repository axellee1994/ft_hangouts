package com.student.ft_hangouts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val databaseHelper = DatabaseHelper(context)

            for (sms in smsMessages) {
                val senderPhone = sms.originatingAddress ?: continue
                val messageText = sms.messageBody

                // Find if this sender matches a saved contact
                val contacts = databaseHelper.getAllContacts()
                // Simple check to see if the saved number is inside the sender string (to handle +65 country codes)
                val matchedContact = contacts.find { 
                    senderPhone.contains(it.phoneNumber.replace("\\s".toRegex(), "")) 
                }

                if (matchedContact != null) {
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val timestamp = sdf.format(Date())

                    // Save the incoming message as a grey bubble (isSent = false)
                    val newMessage = Message(
                        contactId = matchedContact.id,
                        text = messageText,
                        timestamp = timestamp,
                        isSent = false 
                    )
                    databaseHelper.addMessage(newMessage)
                }
            }
        }
    }
}