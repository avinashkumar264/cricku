package com.example.cricku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReplyAdapter(
    private val replyList: List<Reply>
) : RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {

    // ViewHolder
    class ReplyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvUser: TextView =
            itemView.findViewById(R.id.tvReplyUser)

        val tvMessage: TextView =
            itemView.findViewById(R.id.tvReplyMessage)

        val tvTime: TextView =
            itemView.findViewById(R.id.tvReplyTime)
    }

    // Create View
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReplyViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_reply,
                parent,
                false
            )

        return ReplyViewHolder(view)
    }

    // Bind Data
    override fun onBindViewHolder(
        holder: ReplyViewHolder,
        position: Int
    ) {

        val reply = replyList[position]

        holder.tvUser.text = reply.username
        holder.tvMessage.text = reply.text
        holder.tvTime.text = reply.time
    }

    // List Size
    override fun getItemCount(): Int {
        return replyList.size
    }
}