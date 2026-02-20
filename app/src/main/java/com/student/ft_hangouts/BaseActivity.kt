package com.student.ft_hangouts

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Apply the saved color immediately when the activity is created
        applyHeaderColor()
    }

    // 1. Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.header_menu, menu)
        return true
    }

    // 2. Handle menu clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val selectedColor = when (item.itemId) {
            R.id.color_red -> Color.parseColor("#FF5252") // Material Red
            R.id.color_blue -> Color.parseColor("#448AFF") // Material Blue
            R.id.color_green -> Color.parseColor("#4CAF50") // Material Green
            else -> return super.onOptionsItemSelected(item)
        }

        // Save the chosen color
        editor.putInt("header_color", selectedColor).apply()
        
        // Apply it immediately
        applyHeaderColor()
        return true
    }

    // 3. Function to change the Action Bar color
    private fun applyHeaderColor() {
        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        // Default to a dark grey if no color is saved yet
        val defaultColor = Color.parseColor("#37474F") 
        val savedColor = prefs.getInt("header_color", defaultColor)

        // Change the ActionBar color
        supportActionBar?.setBackgroundDrawable(ColorDrawable(savedColor))
        
        // Change the top status bar color to match (optional but looks much better)
        window.statusBarColor = savedColor 
    }

    override fun onResume() {
        super.onResume()
        
        // Retrieve the saved timestamp from SharedPreferences
        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val lastBackgroundTime = prefs.getString("last_background_time", null)

        if (lastBackgroundTime != null) {
            Toast.makeText(this, getString(R.string.last_seen, lastBackgroundTime), Toast.LENGTH_LONG).show()
            prefs.edit().remove("last_background_time").apply()
        }
    }

    override fun onStop() {
        super.onStop()
        
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = sdf.format(Date())

        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("last_background_time", currentTime).apply()
    }
}