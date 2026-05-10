package com.example.cricku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerPosts: RecyclerView
    private lateinit var adapter: PostAdapter

    private val postList = mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCategory = findViewById<Button>(R.id.btnCategory)
        recyclerPosts = findViewById(R.id.recyclerPosts)

        val username = intent.getStringExtra("username") ?: "User"

        btnCategory.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        recyclerPosts.layoutManager = LinearLayoutManager(this)

        adapter = PostAdapter(postList, username)
        recyclerPosts.adapter = adapter

        loadSamplePosts()
    }

    private fun loadSamplePosts() {
        postList.clear()

        postList.add(
            Post(
                username = "CrickU Updates",
                content = "Welcome to CrickU. Tap category to explore cricket, football, hockey and fan discussions.",
                timestamp = "Now",
                category = "General",
                type = "Post"
            )
        )

        for (i in 1..10) {
            val post = Post(
                username = "Fan$i",
                content = "This is sample sports post number $i. Fans can share opinions, match thoughts and quick updates here.",
                timestamp = "Now",
                category = "General",
                type = if (i % 2 == 0) "Stats" else "Prediction"
            )
            postList.add(post)
        }

        adapter.notifyDataSetChanged()
    }
}
