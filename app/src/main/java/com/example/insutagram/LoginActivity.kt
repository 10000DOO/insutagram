package com.example.insutagram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.insutagram.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginActivity:AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val value1 = intent?.getStringExtra("key1")
        println(value1)
        if(value1 !=null)
            binding.userName.setText(value1.toString())

        binding.login.setOnClickListener{

            var userEmail:String
            val password = binding.password.text.toString()


                userEmail = binding.userName.text.toString()
                doLogin(userEmail,password)

        }
        binding.createAccount.setOnClickListener{
            //val userEmail = binding.username.text.toString()
            //val password = binding.password.text.toString()
            //doSignUp(userEmail,password)
            val intent =Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    private fun doLogin(userEmail:String, password:String){
        Firebase.auth.signInWithEmailAndPassword(userEmail,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    startActivity(
                        Intent(this,MainActivity::class.java))
                    finish()
                }
                else{
                    Log.w("LoginActivity","signInWithEmail",it.exception)
                    Toast.makeText(this,"Authentication failed.",Toast.LENGTH_SHORT).show()
                }
            }
    }

}