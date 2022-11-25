package com.example.insutagram

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.insutagram.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class ProfileActivity : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    private lateinit var binding: ActivityProfileBinding
    private var adapter: CustomAdapter? = null
    val db: FirebaseFirestore = Firebase.firestore
    val itemsCollectionRef = db.collection("images")
    val followCollectionRef = db.collection("follow")
    val testCollectionRef = db.collection("test")
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
        storage = FirebaseStorage.getInstance()

        testCollectionRef.document(currentUid).get().addOnSuccessListener {
            val imageRef2 = storage.getReferenceFromUrl(it["profile_img"].toString())
            imageRef2.getBytes(Long.MAX_VALUE).addOnCompleteListener{//successListener도 가능
                if(it.isSuccessful){
                    val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    binding.profileImage.setImageBitmap(bmp)
                }
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