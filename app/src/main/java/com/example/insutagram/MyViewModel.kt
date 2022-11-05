package com.example.insutagram

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.HashMap

data class ContentDTO( var userId: String, var imageUrl: String, var likeCount: Int, var commentsCount: Int)

class MyViewModel : ViewModel(){

    val db : FirebaseFirestore = Firebase.firestore
    val itemCollectionRef = db.collection("post")
    var items = mutableListOf<ContentDTO>()

    init {
        itemCollectionRef.get().addOnSuccessListener {
            for (doc in it){
                items.add(ContentDTO("1@1.com", "adsfadf", 13, 14))
                items.add(ContentDTO("1@2.com", "adsf22f", 14, 11))
            }
        }

    }

    fun addItem(item: ContentDTO){
        items.add(item)
    }

    fun updateList(newList: MutableList<ContentDTO>) {
        items = newList
    }
}