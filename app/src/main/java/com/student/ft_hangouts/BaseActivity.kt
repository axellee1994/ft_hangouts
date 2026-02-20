package com.student.ft_hangouts

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        
        // Retrieve the saved timestamp from SharedPreferences
        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val lastBackgroundTime = prefs.getString("last_background_time", null)

        if (lastBackgroundTime != null) {
            Toast.makeText(this, getString(R.string.last_seen, lastBackgroundTime), Toast.LENGTH_LONG).show()
            
            // Clear the timestamp so the toast doesn't keep appearing 
            // every time you switch internal screens
            prefs.edit().remove("last_background_time").apply()
        }
    }

    override fun onStop() {
        super.onStop()
        
        // If the activity is stopping, it's going to the background.
        // Save the current timestamp.
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = sdf.format(Date())

        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("last_background_time", currentTime).apply()
    }
}