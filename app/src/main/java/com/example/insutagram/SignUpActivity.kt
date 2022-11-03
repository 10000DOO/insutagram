package com.example.insutagram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.insutagram.databinding.ActivitySignupBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUp.setOnClickListener{
            val userEmail = binding.username2.text.toString()
            val password = binding.password2.text.toString()
            doSignUp(userEmail,password)
        }
    }

    private fun doSignUp(userEmail:String, password:String){
        Firebase.auth.createUserWithEmailAndPassword(userEmail,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    startActivity(
                        Intent(this,MainActivity::class.java))
                    finish()
                }
                else{
                    Log.w("LoginActivity","signInWithEmail",it.exception)
                    Toast.makeText(this,"Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}