package com.example.cricku

data class Post(

    var id: String = "",

    var username: String = "",

    var content: String = "",

    var timestamp: String = "",

    var likes: Int = 0,

    var category: String = "General",

    var type: String = "Post",

    var liked: Boolean = false,

    var replies: MutableList<Reply> = mutableListOf()

)
