package com.example.cricku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Choose Category"

        val username = intent.getStringExtra("username") ?: "User"

        val cricket = findViewById<Button>(R.id.btnCricket)
        val football = findViewById<Button>(R.id.btnFootball)
        val hockey = findViewById<Button>(R.id.btnHockey)
        val all = findViewById<Button>(R.id.btnAll)

        cricket.setOnClickListener {
            openOption("Cricket", username)
        }

        football.setOnClickListener {
            openOption("Football", username)
        }

        hockey.setOnClickListener {
            openOption("Hockey", username)
        }

        all.setOnClickListener {
            openOption("All", username)
        }
    }

    private fun openOption(category: String, username: String) {
        val intent = Intent(this, OptionActivity::class.java)
        intent.putExtra("category", category)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
