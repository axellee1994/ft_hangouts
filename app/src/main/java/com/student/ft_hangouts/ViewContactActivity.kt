package com.student.ft_hangouts

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ViewContactActivity : BaseActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private var contactId: Long = -1

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var etInfo: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_contact)

        databaseHelper = DatabaseHelper(this)

        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmail)
        etInfo = findViewById(R.id.etInfo)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)

        // Retrieve the ID passed from MainActivity
        contactId = intent.getLongExtra("CONTACT_ID", -1)

        if (contactId != -1L) {
            loadContactData()
        } else {
            Toast.makeText(this, getString(R.string.error_loading), Toast.LENGTH_SHORT).show()
            finish() // Close the activity if ID is invalid
        }

        btnSave.setOnClickListener {
            updateContact()
        }

        btnDelete.setOnClickListener {
            deleteContact()
        }
    }

    private fun loadContactData() {
        val contact = databaseHelper.getContact(contactId)
        contact?.let {
            etFirstName.setText(it.firstName)
            etLastName.setText(it.lastName)
            etPhone.setText(it.phoneNumber)
            etEmail.setText(it.email)
            etInfo.setText(it.info)
        }
    }

    private fun updateContact() {
        // Create a new Contact object with the updated text fields
        val updatedContact = Contact(
            id = contactId,
            firstName = etFirstName.text.toString().trim(),
            lastName = etLastName.text.toString().trim(),
            phoneNumber = etPhone.text.toString().trim(),
            email = etEmail.text.toString().trim(),
            info = etInfo.text.toString().trim()
        )

        val rowsAffected = databaseHelper.updateContact(updatedContact)
        if (rowsAffected > 0) {
            Toast.makeText(this, getString(R.string.contact_updated), Toast.LENGTH_SHORT).show()
            finish() // Return to the list
        } else {
            Toast.makeText(this, getString(R.string.error_update_failed), Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteContact() {
        val rowsAffected = databaseHelper.deleteContact(contactId)
        if (rowsAffected > 0) {
            Toast.makeText(this, getString(R.string.contact_deleted), Toast.LENGTH_SHORT).show()
            finish() // Return to the list
        } else {
            Toast.makeText(this, getString(R.string.error_delete_failed), Toast.LENGTH_SHORT).show()
        }
    }
}