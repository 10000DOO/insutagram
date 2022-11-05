package com.example.insutagram

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.insutagram.databinding.PostBinding
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.HashMap

class CustomAdapter(private val viewModel: MyViewModel) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: PostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setContents(pos: Int){
            val item = viewModel.items[pos]
            binding.userId.text = item.userId
            binding.likeCount.text = "좋아요 ${item.likeCount}"
            binding.commentCount.text = "댓글 ${item.commentsCount}"
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(layoutInflater, parent,false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContents(position)
    }

    override fun getItemCount(): Int {
        return viewModel.items.size
    }
}