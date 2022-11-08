package com.example.insutagram
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.insutagram.databinding.PostBinding
import com.google.firebase.firestore.QueryDocumentSnapshot


data class ContentDTO( var userId: String, var likeCount: String, var commentsCount: String){
    constructor(doc: QueryDocumentSnapshot) :
            this(doc["userId"].toString(), doc["likeCount"].toString(), doc["commentCount"].toString())
}

class ViewHolder(val binding: PostBinding) : RecyclerView.ViewHolder(binding.root)

class CustomAdapter(private val context: Context, private var items: List<ContentDTO>) :
    RecyclerView.Adapter<ViewHolder>() {

    fun updateList(newList: List<ContentDTO>) {
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
        holder.binding.likeCount.text = item.likeCount
        holder.binding.commentCount.text = item.commentsCount
    }

    override fun getItemCount(): Int {
        return items.size
    }
}