package com.example.insutagram

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.insutagram.dto.Content
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class PostActivity : AppCompatActivity() {

    lateinit var storage : FirebaseStorage
    lateinit var auth : FirebaseAuth
    lateinit var firestore : FirebaseFirestore
    var photoUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        //초기화
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"

        var photoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            //사진 받는 부분
            photoUri = result.data?.data
            findViewById<ImageView>(R.id.imageView).setImageURI(photoUri)
        }

        //사진추가 버튼
        findViewById<Button>(R.id.addphoto_btn_upload).setOnClickListener {
            photoResult.launch(photoPickerIntent)
        }

        //게시 버튼
        findViewById<Button>(R.id.post_btn).setOnClickListener {
            contentUpload()
        }


    }

    //게시하기
    fun contentUpload(){
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "IMAGE_" + timestamp + ".png"
        var storagePath = storage.reference?.child("images")?.child(imageFileName)

//        if (storagePath != null) {
//            storagePath.putFile(photoUri!!).continueWithTask {
//                return@continueWithTask storagePath.downloadUrl
//            }.addOnCompleteListener { downloadUrl ->
//
//
//        }
                var Content = Content()

//                Content.imageUrl = downloadUrl.result.toString()
                Content.post_text = findViewById<EditText>(R.id.addphoto_edit_explain).text.toString()
                Content.uid = auth?.currentUser?.uid
                Content.userId = auth?.currentUser?.email
                Content.timestamp = (System.currentTimeMillis()).toString()

                //collection(Collection 이름). document(Doucument 이름/ 아무것도 안넣으면 무작위 생성).set(입력할 데이터)
                firestore.collection("images").document().set(Content)

                Toast.makeText(this,"업로드에 성공하였습니다.", Toast.LENGTH_LONG).show()

                //페이지 나오기. 액티비티 종료
                finish()

            }
        }

//    }
    fun getImageUri(context : Context, bitmap : Bitmap): Uri? {
        var bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes)
        var path = MediaStore.Images.Media.insertImage(context.contentResolver,bitmap,"Title",null)
        return Uri.parse(path)
    }

