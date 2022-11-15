package com.example.insutagram
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.insutagram.databinding.PostBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


data class ContentDTO( var userId: String, var likeCount: String, var commentsCount: String, var postContent: String){
    constructor(doc: QueryDocumentSnapshot) :
            this(doc["userId"].toString(), doc["likeCount"].toString(), doc["commentCount"].toString(), doc["postContent"].toString())
}

private val db: FirebaseFirestore = Firebase.firestore
private val itemsCollectionRef = db.collection("content")

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
        holder.binding.likeCount.text = "좋아요 ${item.likeCount}"
        holder.binding.commentCount.text = "댓글 ${item.commentsCount}"
        holder.binding.postContent.text = item.postContent
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updatePost() {
        itemsCollectionRef.get().addOnSuccessListener {
            val items = mutableListOf<ContentDTO>()
            for (doc in it) {
                items.add(ContentDTO(doc))
            }
            updateList(items)
        }
    }
}