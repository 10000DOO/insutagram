package com.example.insutagram
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.insutagram.databinding.PostBinding
import com.example.insutagram.dto.Content
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private val db: FirebaseFirestore = Firebase.firestore
private val itemsCollectionRef = db.collection("images")

class ViewHolder(val binding: PostBinding) : RecyclerView.ViewHolder(binding.root)

class CustomAdapter(private val context: Context, private var items: List<Content>) :
    RecyclerView.Adapter<ViewHolder>() {

    fun updateList(newList: List<Content>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items[position]
        holder.binding.userId.text = item.userId
        holder.binding.likeCount.text = "좋아요 ${item.favoriteCount}"
        //holder.binding.commentCount.text = "댓글 ${item.commentsCount}"
        holder.binding.postContent.text = item.post_text
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updatePost() {
        itemsCollectionRef.get().addOnSuccessListener {
            val items = mutableListOf<Content>()
            for (doc in it) {
                items.add(Content(doc))
            }
            updateList(items)
        }
    }
}