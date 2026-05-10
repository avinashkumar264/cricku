package com.example.cricku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCategory = findViewById<Button>(R.id.btnCategory)
        val username = intent.getStringExtra("username") ?: "User"

        btnCategory.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }
}
