package com.example.insutagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.insutagram.databinding.ActivityMainBinding
import com.example.insutagram.databinding.ActivityProfileBinding


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var adapter: CustomAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        binding = ActivityProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.contents.layoutManager = LinearLayoutManager(this)
        adapter = CustomAdapter(this, emptyList())
        binding.contents.adapter = adapter


        adapter!!.profile_updatePost()
        //adapter!!.queryWhere()
    }


}