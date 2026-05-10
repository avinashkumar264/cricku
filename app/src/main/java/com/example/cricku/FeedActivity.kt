package com.example.cricku

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FeedActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var db: FirebaseFirestore

    private lateinit var btnAll: Button
    private lateinit var btnCricket: Button
    private lateinit var btnFootball: Button
    private lateinit var btnHockey: Button

    private var selectedCategory = "All"
    private var username = "User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        db = FirebaseFirestore.getInstance()
        username = intent.getStringExtra("username") ?: "User"

        recyclerView = findViewById(R.id.recyclerViewFeed)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PostAdapter(mutableListOf(), username)
        recyclerView.adapter = adapter

        btnAll = findViewById(R.id.btnAll)
        btnCricket = findViewById(R.id.btnCricket)
        btnFootball = findViewById(R.id.btnFootball)
        btnHockey = findViewById(R.id.btnHockey)

        loadPostsFromFirebase("All")

        btnAll.setOnClickListener {
            selectedCategory = "All"
            loadPostsFromFirebase(selectedCategory)
            Toast.makeText(this, "Showing All Posts", Toast.LENGTH_SHORT).show()
        }

        btnCricket.setOnClickListener {
            selectedCategory = "Cricket"
            loadPostsFromFirebase(selectedCategory)
            Toast.makeText(this, "Showing Cricket Posts", Toast.LENGTH_SHORT).show()
        }

        btnFootball.setOnClickListener {
            selectedCategory = "Football"
            loadPostsFromFirebase(selectedCategory)
            Toast.makeText(this, "Showing Football Posts", Toast.LENGTH_SHORT).show()
        }

        btnHockey.setOnClickListener {
            selectedCategory = "Hockey"
            loadPostsFromFirebase(selectedCategory)
            Toast.makeText(this, "Showing Hockey Posts", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadPostsFromFirebase(selectedCategory)
    }

    private fun loadPostsFromFirebase(category: String) {
        val query = if (category == "All") {
            db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(20)
        } else {
            db.collection("posts")
                .whereEqualTo("category", category)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(20)
        }

        query.get()
            .addOnSuccessListener { documents ->
                val loadedPosts = mutableListOf<Post>()

                for (doc in documents) {
                    val post = Post(
                        id = doc.id,
                        username = doc.getString("username") ?: "User",
                        content = doc.getString("content") ?: "",
                        timestamp = (doc.getLong("timestamp") ?: 0L).toString(),
                        likes = doc.getLong("likes")?.toInt() ?: 0,
                        category = doc.getString("category") ?: "General",
                        type = doc.getString("type") ?: "Post"
                    )

                    loadedPosts.add(post)
                    loadReplies(post)
                }

                adapter.updateData(loadedPosts)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Load failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun loadReplies(post: Post) {
        if (post.id.isEmpty()) return

        db.collection("posts")
            .document(post.id)
            .collection("replies")
            .orderBy("time", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { replyDocs ->
                post.replies.clear()

                for (replyDoc in replyDocs) {
                    val reply = Reply(
                        username = replyDoc.getString("username") ?: "User",
                        text = replyDoc.getString("text") ?: "",
                        time = replyDoc.getString("time") ?: ""
                    )
                    post.replies.add(reply)
                }

                adapter.notifyDataSetChanged()
            }
    }
}
