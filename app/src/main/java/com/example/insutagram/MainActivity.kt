package com.example.insutagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.*
import com.example.insutagram.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private val db: FirebaseFirestore = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //로그인이 안되어있으면 로그인 화면으로 이동
        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

//        binding.navbar.setOnItemSelectedListener { item ->
//            when(item.itemId) {
//                R.id.home_icon ->
//                R.id.search_icon ->
//                R.id.profile_icon ->
//            }
//            true
//        }

        val itemsCollectionRef = db.collection("content")
        itemsCollectionRef.get().addOnSuccessListener {
            var items = mutableListOf<ContentDTO>()
            for (doc in it){
                items.add(ContentDTO(doc))
            }
        }
    }
}
