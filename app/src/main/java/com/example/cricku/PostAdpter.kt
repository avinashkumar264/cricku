package com.example.cricku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostAdapter(
    private val posts: MutableList<Post>,
    private val currentUser: String
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.tvUsername)
        val content: TextView = view.findViewById(R.id.tvContent)
        val time: TextView = view.findViewById(R.id.tvTime)
        val likes: TextView = view.findViewById(R.id.tvLikes)
        val likeBtn: Button = view.findViewById(R.id.btnLike)
        val replyBox: EditText = view.findViewById(R.id.etReply)
        val replyBtn: Button = view.findViewById(R.id.btnReply)
        val replyList: TextView = view.findViewById(R.id.tvReplies)
        val deleteBtn: Button = view.findViewById(R.id.btnDelete)
        val category: TextView? = view.findViewById(R.id.tvCategory)
        val type: TextView? = view.findViewById(R.id.tvType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        holder.username.text = post.username
        holder.content.text = post.content
        holder.time.text = formatTimestamp(post.timestamp)
        holder.likes.text = "👍 Likes: ${post.likes}"
        holder.likeBtn.text = if (post.liked) "👍 Unlike" else "👍 Like"
        holder.replyBtn.text = "💬 Reply"
        holder.deleteBtn.text = "🗑 Delete"

        holder.category?.text = "🏏 ${post.category}"
        holder.type?.text = "📊 ${post.type}"

        holder.likeBtn.setOnClickListener {
            toggleLike(post, holder.adapterPosition, holder)
        }

        holder.replyBtn.setOnClickListener {
            val text = holder.replyBox.text.toString().trim()

            if (text.isEmpty()) {
                Toast.makeText(
                    holder.itemView.context,
                    "Enter reply",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val currentTime = getCurrentTime()

            val reply = Reply(
                username = currentUser,
                text = text,
                time = currentTime
            )

            val replyData = hashMapOf(
                "username" to currentUser,
                "text" to text,
                "time" to currentTime
            )

            if (post.id.isEmpty()) {
                post.replies.add(reply)
                holder.replyBox.text.clear()
                notifyItemChanged(holder.adapterPosition)

                Toast.makeText(
                    holder.itemView.context,
                    "Reply added locally",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            db.collection("posts")
                .document(post.id)
                .collection("replies")
                .add(replyData)
                .addOnSuccessListener {
                    post.replies.add(reply)
                    holder.replyBox.text.clear()
                    notifyItemChanged(holder.adapterPosition)

                    Toast.makeText(
                        holder.itemView.context,
                        "Reply saved",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        holder.itemView.context,
                        "Failed to save reply: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        holder.deleteBtn.setOnClickListener {
            val pos = holder.adapterPosition

            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            if (post.id.isEmpty()) {
                posts.removeAt(pos)
                notifyItemRemoved(pos)

                Toast.makeText(
                    holder.itemView.context,
                    "Post removed from list",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            db.collection("posts")
                .document(post.id)
                .delete()
                .addOnSuccessListener {
                    posts.removeAt(pos)
                    notifyItemRemoved(pos)

                    Toast.makeText(
                        holder.itemView.context,
                        "Post deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        holder.itemView.context,
                        "Delete failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        updateReplies(holder, post)
    }

    private fun toggleLike(post: Post, position: Int, holder: PostViewHolder) {
        if (position == RecyclerView.NO_POSITION) return

        val newLikedValue = !post.liked
        val newLikesCount = if (newLikedValue) post.likes + 1 else maxOf(post.likes - 1, 0)

        if (post.id.isEmpty()) {
            post.liked = newLikedValue
            post.likes = newLikesCount
            notifyItemChanged(position)
            return
        }

        db.collection("posts")
            .document(post.id)
            .update(
                mapOf(
                    "likes" to newLikesCount
                )
            )
            .addOnSuccessListener {
                post.liked = newLikedValue
                post.likes = newLikesCount
                notifyItemChanged(holder.adapterPosition)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    holder.itemView.context,
                    "Like update failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun updateReplies(holder: PostViewHolder, post: Post) {
        if (post.replies.isEmpty()) {
            holder.replyList.text = "No replies yet"
            return
        }

        val builder = StringBuilder()

        for (reply in post.replies) {
            builder.append("${reply.username} (${reply.time}): ${reply.text}\n")
        }

        holder.replyList.text = builder.toString().trim()
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun formatTimestamp(timestamp: String): String {
        return try {
            val millis = timestamp.toLong()
            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            sdf.format(Date(millis))
        } catch (e: Exception) {
            timestamp
        }
    }

    fun updateData(newPosts: List<Post>) {
        posts.clear()
        posts.addAll(newPosts)
        notifyDataSetChanged()
    }
}
