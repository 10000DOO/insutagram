package com.example.insutagram

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.insutagram.databinding.ActivityLoginBinding
import com.example.insutagram.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


private val db: FirebaseFirestore = Firebase.firestore
lateinit var storage: FirebaseStorage
var photoUri: Uri? = null

class SignUpActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //초기화
        storage = FirebaseStorage.getInstance()

        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"

        var photoResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                //사진 받는 부분
                photoUri = result.data?.data
                findViewById<ImageButton>(R.id.imageButton).setImageURI(photoUri)
            }

        //사진추가 버튼
        binding.imageButton.setOnClickListener {
            photoResult.launch(photoPickerIntent)
        }


        binding.signUp.setOnClickListener {
            val userEmail = binding.userName2.text.toString()
            val password = binding.password2.text.toString()


            doSignUp(userEmail, password)
        }
    }

    private fun doSignUp(userEmail: String, password: String) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "profile_img_" + timestamp + ".png"
        var storagePath = storage.reference?.child("profile_img")?.child(imageFileName)

        println(storagePath)
        if (storagePath != null) {
            storagePath.putFile(photoUri!!).continueWithTask {
                return@continueWithTask storagePath.downloadUrl
            }.addOnCompleteListener { downloadUrl ->


                Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            //유아이디 가져오고
                            var currentUid = FirebaseAuth.getInstance().currentUser!!.uid

                            val itemMap = hashMapOf(
                                "uid" to currentUid,
                                "name" to userEmail,
                                "profile_img" to downloadUrl.result.toString()
                            )

                            //db에 넣기
                            println(itemMap)
                            val follow = FollowDTO()
                            db.collection("test").document(currentUid).set(itemMap)
                            db.collection("follow").document(currentUid!!).set(follow)
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.putExtra("key1", userEmail)
                            startActivity(
                                intent
                            )
                            finish()
                        } else {
                            Log.w("LoginActivity", "signInWithEmail", it.exception)
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }
        else if(storagePath == null){
            println("사진 선택 안함")
        }
    }
}