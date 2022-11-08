package com.example.insutagram

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //글쓰기 화면으로 이동
        val fab: View = findViewById(R.id.post_btn)
        fab.setOnClickListener { view ->

            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
        }
    }
}
