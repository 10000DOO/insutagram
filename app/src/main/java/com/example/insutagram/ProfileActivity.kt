package com.example.insutagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.insutagram.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var adapter: CustomAdapter? = null
    val db: FirebaseFirestore = Firebase.firestore
    val itemsCollectionRef = db.collection("images")
    val followCollectionRef = db.collection("follow")
    var currentUid :String = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        binding = ActivityProfileBinding.inflate(layoutInflater)


        binding.navbar.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.home_icon){
                startActivity(Intent(this, MainActivity::class.java))
            }else if (item.itemId == R.id.add_post){
                startActivity(Intent(this, PostActivity::class.java))
            }
            else if (item.itemId == R.id.search_icon){
                startActivity(Intent(this, SearchActivity::class.java))
            }
            true
        }

        setContentView(binding.root)

        var count = 0
        itemsCollectionRef.get().addOnSuccessListener {
            for (doc in it) {
                if (doc["uid"].toString() == currentUid)
                    count++
                binding.accountPostTextview.text = count.toString()
            }
        }

        followCollectionRef.document(currentUid).get().addOnSuccessListener {
            if (it["followerCount"].toString() != null){
                binding.accountFollowerTextview.text = it["followerCount"].toString()
            }
            if (it["followingCount"].toString() != null){
                binding.accountFollowingTextview.text = it["followingCount"].toString()
            }

        }

        binding.contents.layoutManager = LinearLayoutManager(this)
        adapter = CustomAdapter(this, emptyList())
        binding.contents.adapter = adapter



        adapter!!.profile_updatePost()
        //adapter!!.queryWhere()
    }


}