package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

class PostRepositoryRoomImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val type = object : TypeToken<List<Post>>() {}.type

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999/"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        val request = okhttp3.Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()
        val response = client.newCall(request).execute()
        val responseText = response.body?.string() ?: error(R.string.response_body_null)
        return gson.fromJson(responseText, type)
    }

    override fun likeById(post: Post): Post {
        val request: Request
        if (post.likedByMe) {
            request = okhttp3.Request.Builder()
                .url("${BASE_URL}api/posts/${post.id}/likes")
                .delete(gson.toJson(post.id).toRequestBody(jsonType))
                .build()
        } else {
            request = okhttp3.Request.Builder()
            .url("${BASE_URL}api/posts/${post.id}/likes")
            .post(gson.toJson(post.id).toRequestBody(jsonType))
            .build()
        }
        val response = client.newCall(request).execute()
        val responseText = response.body?.string() ?: error(R.string.response_body_null)
        val post =gson.fromJson(responseText, Post::class.java)
        return post
    }

    override fun shareById(id: Long) {
        // TODO:
    }

    override fun removeById(id: Long) {
        val request = okhttp3.Request.Builder()
            .url("${BASE_URL}api/posts/$id")
            .delete(gson.toJson("").toRequestBody(jsonType))
            .build()
        val response = client.newCall(request).execute()
    }

    override fun save(post: Post): Post {
        val request = okhttp3.Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()
        val response = client.newCall(request).execute()
        val responseText = response.body?.string() ?: error(R.string.response_body_null)
        return gson.fromJson(responseText, Post::class.java)
    }
}