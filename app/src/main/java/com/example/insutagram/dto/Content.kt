package com.example.insutagram.dto

import com.google.firebase.firestore.QueryDocumentSnapshot

data class Content (
    var post_text : String? = null, //포스팅 글
    var imageUrl : String? = null, //사진 다운로드 주소
    var uid : String? = null, //Following, Follow
    var userId : String? = null, //you6878@icloud.com
    var timestamp : String? = null, //업로드 시간
    var favoriteCount : String? = null, //좋아요 카운트
    var favorites : MutableMap<String,Boolean> = HashMap() // 좋아요 적용, 취소 기능 위해서 존재
    ) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc["post_text"].toString(), doc["imageUrl"].toString(), doc["uid"].toString(),
                doc["userId"].toString(), doc["timestamp"].toString(), doc["favoriteCount"].toString(),)
}


//data class Content( var userId: String, var likeCount: String, var commentsCount: String, var postContent: String){
//    constructor(doc: QueryDocumentSnapshot) :
//            this(doc["userId"].toString(), doc["likeCount"].toString(), doc["commentCount"].toString(), doc["postContent"].toString())
//}