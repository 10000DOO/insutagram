package com.example.insutagram

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // 핸들러를 통해서 지정한 시간 이후 화면 이동
        Handler(Looper.getMainLooper()).postDelayed({

            // 지정한 시간이 지나면 다음 액티비티로 이동
            val intent= Intent( this,MainActivity::class.java)
            startActivity(intent)

            // 스플래시 화면 finish
            finish()

        }, 1000) // 타이머 지정 : 2초 Duration

        println("스플래쉬~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")


    }
}