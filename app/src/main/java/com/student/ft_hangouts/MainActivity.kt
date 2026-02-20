package com.student.ft_hangouts

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var contactListView: ListView
    private lateinit var fabAddContact: FloatingActionButton
    private lateinit var adapter: ArrayAdapter<Contact> // Make adapter a class property

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaseHelper = DatabaseHelper(this)
        contactListView = findViewById(R.id.contactListView)
        fabAddContact = findViewById(R.id.fabAddContact)

        // Set up the Add button click listener
        fabAddContact.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)
        }

        // The setOnItemClickListener must be inside a method, like onCreate.
        contactListView.setOnItemClickListener { _, _, position, _ ->
            val selectedContact = adapter.getItem(position)
            selectedContact?.let {
                val intent = Intent(this, ViewContactActivity::class.java)
                intent.putExtra("CONTACT_ID", it.id) // Pass the ID to the next screen
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload contacts every time we return to this screen
        loadContacts()
    }

    private fun loadContacts() {
        val contacts = databaseHelper.getAllContacts()

        // Create an adapter to map the Contact data to the XML layout elements
        // Initialize the adapter property here
        adapter = object : ArrayAdapter<Contact>(this, R.layout.contact_list_item, R.id.tvName, contacts) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val contact = getItem(position)

                val tvName = view.findViewById<TextView>(R.id.tvName)
                val tvPhone = view.findViewById<TextView>(R.id.tvPhone)

                tvName.text = "${contact?.firstName} ${contact?.lastName}"
                tvPhone.text = contact?.phoneNumber

                return view
            }
        }

        contactListView.adapter = adapter
    }
}
