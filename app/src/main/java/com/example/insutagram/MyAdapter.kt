package com.example.insutagram

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.insutagram.databinding.UserListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.HashMap



data class UserDTO( var uid: String,var name: String){
    constructor(doc: QueryDocumentSnapshot) :
            this(doc["uid"].toString(), doc["name"].toString())
}
data class FollowDTO(
    //var name:String,
    //var uid:String,
    var followerCount: Int = 0,
    var followers: MutableMap<String, Boolean> = HashMap(),

    var followingCount: Int = 0,
    var followings: MutableMap<String, Boolean> = HashMap()
)

private val db: FirebaseFirestore = Firebase.firestore
private val itemsCollectionRef = db.collection("follow")
class ViewHolder1(val binding: UserListBinding) : RecyclerView.ViewHolder(binding.root)
var followListenerRegistration: ListenerRegistration? = null
lateinit var binding:UserListBinding
lateinit var otherId:String


class MyAdapter(private val context: Context, private var items: List<UserDTO>) :
    RecyclerView.Adapter<ViewHolder1>() {
    var currentUid = FirebaseAuth.getInstance().currentUser!!.uid

    fun updateList(newList: List<UserDTO>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder1 {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = UserListBinding.inflate(layoutInflater, parent, false)

        return ViewHolder1(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder1, position: Int) {
        var item = items[position]
        holder.binding.userName.text=item.name
        db.collection("follow").document(item.uid).get().addOnSuccessListener {
            var followDTO = it.toObject(FollowDTO::class.java)
            println(followDTO)
            if(followDTO!!.followers.containsKey(currentUid))
                holder.binding.toggleButton.isChecked=true


        }
        holder.binding.toggleButton.setOnClickListener{
            println("${position} 클릭함")
            requestFollow(item.uid)
        }


    }

    override fun getItemCount(): Int {
        return items.size
    }

    //입력 받은 uid를 제외한 uid들만 데이터 가져옴
    fun queryWhere(currentId:String){
        val myId = currentId
        db.collection("test").whereNotEqualTo("uid",myId).get()
            .addOnSuccessListener {
                val items = mutableListOf<UserDTO>()
                for (doc in it) {
                    items.add(UserDTO(doc))
                    //items.add(FollowDTO('${doc["uid"]}'))
                }
                updateList(items)
            }
    }
    fun requestFollow(otherid: String) {
        var tsDocFollowing = db!!.collection("follow").document(currentUid!!)
        db?.runTransaction { transaction ->
            var followDTO = transaction.get(tsDocFollowing).toObject(FollowDTO::class.java)
            if (followDTO == null) {
                followDTO = FollowDTO()
                followDTO.followingCount = 1
                followDTO.followings[otherid!!] = true
                println("새로 생성됨")
                transaction.set(tsDocFollowing, followDTO)
                return@runTransaction
            }
            if (followDTO?.followings?.containsKey(otherid!!)!!) {
                followDTO!!.followingCount = followDTO!!.followingCount - 1
                followDTO!!.followings.remove(otherid!!)
            } else {
                followDTO!!.followingCount = followDTO!!.followingCount + 1
                followDTO!!.followings[otherid!!] = true
            }
            transaction.set(tsDocFollowing, followDTO)
            return@runTransaction
        }
        var tsDocFollower = db!!.collection("follow").document(otherid!!)
        db?.runTransaction { transaction ->
            var followDTO = transaction.get(tsDocFollower).toObject(FollowDTO::class.java)
            if (followDTO == null) {
                followDTO = FollowDTO()
                followDTO!!.followerCount = 1
                followDTO!!.followers[currentUid!!] = true
                transaction.set(tsDocFollower, followDTO!!)
                return@runTransaction
            }
            if (followDTO?.followers?.containsKey(currentUid!!)!!) {
                followDTO!!.followerCount = followDTO!!.followerCount - 1
                followDTO!!.followers.remove(currentUid!!)
            } else {
                followDTO!!.followerCount = followDTO!!.followerCount + 1
                followDTO!!.followers[currentUid!!] = true
            }
            transaction.set(tsDocFollower, followDTO!!)
            return@runTransaction
        }
    }
    /*@SuppressLint("ResourceType")
    fun getFollowerAndFollowing(otherid: String){
        followListenerRegistration = db?.collection("follow")?.document(otherid!!)?.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            var followDTO = documentSnapshot?.toObject(FollowDTO::class.java)
            if(followDTO == null)return@addSnapshotListener
            if(followDTO?.followers?.containsKey(currentUid)!!){
                println("팔로우된 상태 ")
                //binding.followBtn.text = context.resources.getString(R.string.follow_cancel)
                //binding.followBtn.text = "팔로우 취소"
                //binding.followBtn.setText(R.string.follow_cancel)
                //binding.toggleButton.isChecked=false
            }
            else{
                println("팔로우 안된 상태")

                if(otherid !=currentUid){
                    println("팔로우 안된 상태1")
                    binding.followBtn.text =context.resources.getString(R.string.follow)
                    //binding.followBtn.text = "팔로우 가능"
                    //binding.toggleButton.isChecked=true



                }
            }
        }
    }
    fun isFollow(otherid:String){
        itemsCollectionRef.document(otherid!!).get().addOnSuccessListener {
            var followDTO = it.toObject(FollowDTO::class.java)
            if(followDTO == null)return@addOnSuccessListener
            println(followDTO)
            println(followDTO!!.followers.containsKey(currentUid))
            if(followDTO!!.followers.containsKey(currentUid)){
                binding.toggleButton.isChecked = true
            }
            else if(!followDTO!!.followers.containsKey(currentUid)){
                binding.toggleButton.isChecked = false
            }
        }
    }*/

}