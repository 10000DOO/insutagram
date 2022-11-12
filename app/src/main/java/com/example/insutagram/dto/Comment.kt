package com.example.insutagram.dto

data class Comment(
    var uid: String? = null,
    var userId: String? = null,
    var comment: String? = null,
    var timestamp: Long? = null
)