package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val likedByMe: Boolean = false,
    val published: String,
    val likes: Int = 0,
    val shareCount: Int = 0,
    val visibilityCount: Int = 0,
    val urlVideo: String? = null
)
