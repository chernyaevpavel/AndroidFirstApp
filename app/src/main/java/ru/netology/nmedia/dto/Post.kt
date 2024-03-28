package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
    var likeCount: Int = 0,
    var shareCount: Int = 0,
    var visibilityCount: Int = 0
)
