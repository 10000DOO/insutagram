package com.example.insutagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.insutagram.databinding.ActivitySearchBinding
import com.example.insutagram.databinding.PostDetailBinding
import com.example.insutagram.dto.Content
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DetailActivity: AppCompatActivity() {

    private val db: FirebaseFirestore = Firebase.firestore
    private lateinit var binding: PostDetailBinding
    var storage = Firebase.storage
    private val testCollectionRef = db.collection("test")
    private val followCollectionRef = db.collection("follow")
    private val itemsCollectionRef = db.collection("images")
    var favoriteClick = HashMap<String,Boolean>()
    var currentUid = FirebaseAuth.getInstance().currentUser!!.uid


    var uid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val value1 = intent?.getStringExtra("key1")
        val user = intent.getSerializableExtra("key1") as Content? // 직렬화된 객체를 받는 방법
        val postID = intent.getStringExtra("postID")
        println("$$$$$$$$$$"+postID)
        // 프로필 이름 출력
        binding.userName.text= user!!.userId
        // 프로필 사진 출력
        testCollectionRef.document(user.uid!!).get().addOnSuccessListener {
            val imageRef2 = storage.getReferenceFromUrl(it["profile_img"].toString())
            imageRef2.getBytes(Long.MAX_VALUE).addOnCompleteListener{//successListener도 가능
                if(it.isSuccessful){
                    val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    binding.userImage.setImageBitmap(bmp)
                }
            }
        }
        // 게시글 사진 출력
        itemsCollectionRef.document(postID!!).get().addOnSuccessListener {
            val imageRef2 = storage.getReferenceFromUrl(it["imageUrl"].toString())
            imageRef2.getBytes(Long.MAX_VALUE).addOnCompleteListener{//successListener도 가능
                if(it.isSuccessful){
                    val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    binding.postMainImage.setImageBitmap(bmp)
                }
            }
        }
        //좋아요
        binding.likeCount.text = "좋아요 ${user.favoriteCount}"
        if (user.favoriteCheck.get(currentUid) == true){
            binding.favoriteButton.setImageResource(R.drawable.ic_favorite)
        }else{
            binding.favoriteButton.setImageResource(R.drawable.ic_favorite_border)
        }
        binding.favoriteButton.setOnClickListener {
            if (user.favoriteCheck.get(currentUid) == null || user.favoriteCheck.get(currentUid) == false){
                binding.favoriteButton.setImageResource(R.drawable.ic_favorite)
                //itemsCollectionRef.document(postID).update("favoriteCount",(user.favoriteCount?.toInt()?.plus(1)))
                itemsCollectionRef.document(postID).update("favoriteClick",user.favoriteCount+1)
                favoriteClick.put(currentUid, true)
                itemsCollectionRef.document(postID).update("favoriteCheck",favoriteClick)
                CustomAdapter(this, emptyList()).updatePost()
            }else{
                binding.favoriteButton.setImageResource(R.drawable.ic_favorite_border)
                itemsCollectionRef.document(postID).update("favoriteCount",(user.favoriteCount?.toInt()?.minus(1)))
                favoriteClick.put(currentUid, false)
                itemsCollectionRef.document(postID).update("favoriteCheck",favoriteClick)
                CustomAdapter(this, emptyList()).updatePost()
            }
        }



        //본문 내용 출력
        binding.postMainContent.text= user!!.post_text

    }
}
