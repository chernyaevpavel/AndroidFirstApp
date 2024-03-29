package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val likedByMe: Boolean = false,
    val published: String,
    val likeCount: Int = 0,
    val shareCount: Int = 0,
    val visibilityCount: Int = 0
)