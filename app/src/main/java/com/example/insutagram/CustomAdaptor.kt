package com.example.insutagram
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.insutagram.databinding.PostBinding
import com.example.insutagram.dto.Content
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

private val db: FirebaseFirestore = Firebase.firestore
private val itemsCollectionRef = db.collection("images")
private val followCollectionRef = db.collection("follow")
private val testCollectionRef = db.collection("test")
var currentUid = FirebaseAuth.getInstance().currentUser!!.uid

class ViewHolder(val binding: PostBinding) : RecyclerView.ViewHolder(binding.root)


class CustomAdapter(private val context: Context, private var items: List<Content>) :


    RecyclerView.Adapter<ViewHolder>() {

    var storage = Firebase.storage
    var contentUidList = arrayListOf<String>()
    var favoriteClick = HashMap<String,Boolean>()
    var image = arrayListOf<String>()



    fun updateList(newList: List<Content>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //println("%%%%%%%%%%%%%%%"+contentUidList[position])
        var item = items[position]
        testCollectionRef.document(item.uid!!).get().addOnSuccessListener {
            val imageRef2 = storage.getReferenceFromUrl(it["profile_img"].toString())
            imageRef2.getBytes(Long.MAX_VALUE).addOnCompleteListener{//successListener도 가능
                if(it.isSuccessful){
                    val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    holder.binding.profileImage.setImageBitmap(bmp)
                }
            }
        }
        holder.binding.userId.text = item.userId
        holder.binding.likeCount.text = "좋아요 ${item.favoriteCount}"
        holder.binding.postContent.text = item.post_text
        //게시글 클릭시 상세 페이지로 이동

        holder.binding.contentImage.setOnClickListener{
            println(item.uid)
            val intent = Intent(holder.binding.root.context,DetailActivity::class.java)
            println(intent)
            intent.putExtra("key1", item)
            intent.putExtra("postID",contentUidList[position])
            holder.binding.root.context.startActivity(intent)

            //startActivity(context,intent,null)
            //intent.run{binding.root.context.startActivity(this)}
        }
        itemsCollectionRef.document(contentUidList[position]).get().addOnSuccessListener {
            val imageRef2 = storage.getReferenceFromUrl(it["imageUrl"].toString())
            imageRef2.getBytes(Long.MAX_VALUE).addOnCompleteListener{//successListener도 가능
                if(it.isSuccessful){
                    val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    holder.binding.contentImage.setImageBitmap(bmp)
                }
            }

        }
        //fun checkLike(){
            if (item.favoriteCheck.get(currentUid) == true){
                holder.binding.favoriteButton.setImageResource(R.drawable.ic_favorite)
            }else{
                holder.binding.favoriteButton.setImageResource(R.drawable.ic_favorite_border)
            }
            holder.binding.favoriteButton.setOnClickListener {
                if (item.favoriteCheck.get(currentUid) == null || item.favoriteCheck.get(currentUid) == false){
                    holder.binding.favoriteButton.setImageResource(R.drawable.ic_favorite)
                    itemsCollectionRef.document(contentUidList[position]).update("favoriteCount",(item.favoriteCount?.toInt()?.plus(1)))
                    favoriteClick.put(currentUid, true)
                    itemsCollectionRef.document(contentUidList[position]).update("favoriteCheck",favoriteClick)
                    updatePost()
                }else{
                    holder.binding.favoriteButton.setImageResource(R.drawable.ic_favorite_border)
                    itemsCollectionRef.document(contentUidList[position]).update("favoriteCount",(item.favoriteCount?.toInt()?.minus(1)))
                    favoriteClick.put(currentUid, false)
                    itemsCollectionRef.document(contentUidList[position]).update("favoriteCheck",favoriteClick)
                    updatePost()
                }
            }
        //}
        //checkLike()

    }


    override fun getItemCount(): Int {
        return items.size
    }

    //자신의 uid와 팔로우 한 uid들만 데이터 가져옴
    fun updatePost() {
        var followDTO: FollowDTO
        var itemss : MutableList<Content> = mutableListOf<Content>()
        itemsCollectionRef.get().addOnSuccessListener {

            //val items = mutableListOf<Content>()
            for (doc in it) {
                //println("followkey = " + Content(doc).uid)
                followCollectionRef.document(Content(doc).uid!!).get().addOnSuccessListener {
                    followDTO = it.toObject(FollowDTO::class.java)!!
                    if(followDTO.followers.containsKey(currentUid)){
                        println("있어")
                        itemss.add(Content(doc))
                        contentUidList.add(doc.id)
                        //println("*******************************************"+itemss)
                        updateList(itemss)
                    }
                    else if(!followDTO.followers.containsKey(currentUid))
                        println("없어")
                }
                //자신의 게시글 불러옴
                if((Content(doc).uid == currentUid)){
                    itemss.add(Content(doc))
                    contentUidList.add(doc.id)
                    updateList(itemss)
                }
            }
            updateList(itemss)
        }
    }

    fun profile_updatePost() {
        var followDTO: FollowDTO
        var itemss : MutableList<Content> = mutableListOf<Content>()
        itemsCollectionRef.get().addOnSuccessListener {

            //val items = mutableListOf<Content>()
            for (doc in it) {

                //자신의 게시글 불러옴
                if((Content(doc).uid == currentUid)){
                    itemss.add(Content(doc))
                    contentUidList.add(doc.id)
                    updateList(itemss)
                }
            }
            updateList(itemss)
        }
    }

}