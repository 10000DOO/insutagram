package com.example.insutagram

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.insutagram.databinding.ActivityDetailBinding
import com.example.insutagram.databinding.ActivityMainBinding
import com.example.insutagram.databinding.PostDetailBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DetailActivity : AppCompatActivity(){
    private lateinit var binding: ActivityDetailBinding
    private var adapter: DetailAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //로그인이 안되어있으면 로그인 화면으로 이동
        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

        binding.navbar.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.home_icon){
                startActivity(Intent(this, MainActivity::class.java))
            }else if (item.itemId == R.id.add_post){
                startActivity(Intent(this, PostActivity::class.java))
            }
            else if (item.itemId == R.id.search_icon){
                startActivity(Intent(this, SearchActivity::class.java))
            }
            else if (item.itemId == R.id.profile_icon){
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            true
        }
        binding.contents.layoutManager = LinearLayoutManager(this)
        adapter = DetailAdapter(this, emptyList())
        binding.contents.adapter = adapter


        adapter!!.updatePost()
        //adapter!!.queryWhere()
    }
}
