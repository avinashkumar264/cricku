package com.example.cricku

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PostActivity : AppCompatActivity() {

    private lateinit var etPost: EditText
    private lateinit var btnPost: Button
    private lateinit var btnStats: Button
    private lateinit var btnPrediction: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter

    private val posts = mutableListOf<Post>()

    private var username: String = "User"
    private var category: String = "General"
    private var type: String = "Stats"

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = FirebaseFirestore.getInstance()

        username = intent.getStringExtra("username") ?: "User"
        category = intent.getStringExtra("category") ?: "General"
        type = intent.getStringExtra("type") ?: "Stats"

        etPost = findViewById(R.id.etPost)
        btnPost = findViewById(R.id.btnPost)
        btnStats = findViewById(R.id.btnStats)
        btnPrediction = findViewById(R.id.btnPrediction)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PostAdapter(mutableListOf(), username)
        recyclerView.adapter = adapter

        highlightSelectedType()
        loadPosts()

        btnStats.setOnClickListener {
            type = "Stats"
            highlightSelectedType()
            loadPosts()
        }

        btnPrediction.setOnClickListener {
            type = "Prediction"
            highlightSelectedType()
            loadPosts()
        }

        btnPost.setOnClickListener {
            val postText = etPost.text.toString().trim()

            if (postText.isEmpty()) {
                Toast.makeText(this, "Please enter post", Toast.LENGTH_SHORT).show()
            } else {
                savePost(postText)
            }
        }
    }

    private fun highlightSelectedType() {
        if (type.equals("Stats", ignoreCase = true)) {
            btnStats.alpha = 1.0f
            btnPrediction.alpha = 0.5f
            etPost.hint = "Share match stats, score updates or analysis..."
        } else {
            btnPrediction.alpha = 1.0f
            btnStats.alpha = 0.5f
            etPost.hint = "Share your prediction with fans..."
        }
    }

    private fun savePost(postText: String) {
        val time = System.currentTimeMillis()

        val postData = hashMapOf(
            "username" to username,
            "category" to category,
            "type" to type,
            "content" to postText,
            "timestamp" to time,
            "likes" to 0
        )

        db.collection("posts")
            .add(postData)
            .addOnSuccessListener {
                etPost.text.clear()
                Toast.makeText(this, "Post saved successfully", Toast.LENGTH_SHORT).show()
                loadPosts()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save post: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun loadPosts() {
        db.collection("posts")
            .whereEqualTo("category", category)
            .whereEqualTo("type", type)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                posts.clear()

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

                    posts.add(post)
                    loadReplies(post)
                }

                adapter.updateData(posts.toList())

                if (posts.isNotEmpty()) {
                    recyclerView.scrollToPosition(0)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Load failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun loadReplies(post: Post) {
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
            .addOnFailureListener {
                adapter.notifyDataSetChanged()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
