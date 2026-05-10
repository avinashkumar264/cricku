package com.example.cricku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class OptionActivity : AppCompatActivity() {

    private var username: String = "User"
    private var category: String = "General"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Choose Post Type"

        username = intent.getStringExtra("username") ?: "User"
        category = intent.getStringExtra("category") ?: "General"

        val btnStats = findViewById<Button>(R.id.btnStats)
        val btnPrediction = findViewById<Button>(R.id.btnPrediction)

        btnStats.setOnClickListener {
            openPost("Stats")
        }

        btnPrediction.setOnClickListener {
            openPost("Prediction")
        }
    }

    private fun openPost(type: String) {
        val intent = Intent(this@OptionActivity, PostActivity::class.java)
        intent.putExtra("type", type)
        intent.putExtra("username", username)
        intent.putExtra("category", category)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
