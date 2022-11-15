

package com.example.insutagram

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.insutagram.databinding.ActivityMainBinding
import com.example.insutagram.databinding.ActivitySearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity: AppCompatActivity() {
    lateinit var firestore : FirebaseFirestore
    lateinit var currentUid :String

    private lateinit var binding: ActivitySearchBinding
    private var adapter: MyAdapter? = null

    var uid : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navbar.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.home_icon){
                startActivity(Intent(this, MainActivity::class.java))
            }else if (item.itemId == R.id.add_post){
                startActivity(Intent(this, PostActivity::class.java))
            }
            else if (item.itemId == R.id.search_icon){
                startActivity(Intent(this, SearchActivity::class.java))
            }
            true
        }
        currentUid = FirebaseAuth.getInstance().currentUser!!.uid
        firestore = FirebaseFirestore.getInstance()

        //uid = intent.getStringExtra("destinationUid")
        println(currentUid)
       /* findViewById<Button>(R.id.follow_btn).setOnClickListener {
            println(currentUid)
        }*/

        binding.contents.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(this, emptyList())
        binding.contents.adapter = adapter
        //adapter!!.updatePost()
        adapter!!.queryWhere(currentUid)
    }

    /*fun requestFollow(){
        var tsDocFollowing = firestore!!.collection("test").document(currentUid!!)
        firestore?.runTransaction { transaction ->
            var followDTO = transaction.get(tsDocFollowing).toObject(FollowDTO::class.java)
            if(followDTO == null){
                followDTO = FollowDTO()
                followDTO.followingCount =1
                followDTO.followings[uid!!]=true
                transaction.set(tsDocFollowing, followDTO)
                return@runTransaction
            }
            //내가 이미 상대방을 팔로잉 하고있는 경우 uid 는 상대방 uid
            if (followDTO?.followings?.containsKey(uid)!!) {

                followDTO?.followingCount = followDTO?.followingCount?.minus(1)!!
                followDTO?.followings!!.remove(uid)
            }
            //내가 상대방을 팔로잉 하고 있지 않는 경우우
            else {

                followDTO?.followingCount = followDTO?.followingCount?.plus(1)!!
                followDTO?.followings!![uid!!] = true
            }
            //transaction.set(tsDocFollowing, followDTO)
            //return@runTransaction
        }
    }*/


}