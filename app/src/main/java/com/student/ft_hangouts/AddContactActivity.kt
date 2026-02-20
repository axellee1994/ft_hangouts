package com.student.ft_hangouts

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddContactActivity : BaseActivity() {

    // We declare the database helper here so we can use it in onCreate
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        dbHelper = DatabaseHelper(this)

        val etFirstName = findViewById<EditText>(R.id.et_first_name)
        val etLastName = findViewById<EditText>(R.id.et_last_name)
        val etPhone = findViewById<EditText>(R.id.et_phone)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etInfo = findViewById<EditText>(R.id.et_info)
        val btnSubmit = findViewById<Button>(R.id.submit_button)

        // 3. Set the Click Listener
        btnSubmit.setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val phone = etPhone.text.toString()
            val email = etEmail.text.toString()
            val info = etInfo.text.toString()

            if (firstName.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_enter_name), Toast.LENGTH_SHORT).show()
            }
            else {
                // We pass '0' for ID because the database will generate the real one.
                val newContact = Contact(0, firstName, lastName, phone, email, info)

                dbHelper.addContact(newContact)
                Toast.makeText(this, getString(R.string.contact_saved), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}