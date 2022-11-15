package com.example.insutagram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.insutagram.databinding.ActivityLoginBinding
import com.example.insutagram.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


private val db: FirebaseFirestore = Firebase.firestore

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
                    //유아이디 가져오고
                    var currentUid = FirebaseAuth.getInstance().currentUser!!.uid
                    val itemMap = hashMapOf(
                        "uid" to currentUid,
                        "name" to userEmail
                    )
                    //db에 넣기
                    println(itemMap)
                    db.collection("test").document(currentUid).set(itemMap)
                    val intent =Intent(this,LoginActivity::class.java)
                    intent.putExtra("key1",userEmail)
                    startActivity(
                        intent
                    )
                    finish()
                }
                else{
                    Log.w("LoginActivity","signInWithEmail",it.exception)
                    Toast.makeText(this,"Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}