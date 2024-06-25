package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post
import java.lang.Exception

interface PostRepository {
    fun getAll(callback: NMediaCallback<List<Post>>)
    fun likeById(post: Post, callback: NMediaCallback<Post>)
    fun shareById(id: Long)
    fun removeById(id: Long, callback: NMediaCallback<Unit>)
    fun save(post: Post, callback: NMediaCallback<Post>)

    interface NMediaCallback<T> {
        fun onSuccess(data: T)
        fun onError(e: Exception)
    }
}