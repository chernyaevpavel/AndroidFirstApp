package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryRoomImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val type = object : TypeToken<List<Post>>() {}.type

    companion object {
        const val BASE_URL = "http://10.0.2.2:9999/"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(callback: PostRepository.NMediaCallback<List<Post>>) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) = try {
                    callback.onSuccess(gson.fromJson(response.body?.string(), type))
                } catch (e: Exception) {
                    callback.onError(e)
                }
            })
    }

    override fun likeById(post: Post, callback: PostRepository.NMediaCallback<Post>) {
        val request = if (post.likedByMe) {
            Request.Builder()
                .url("${BASE_URL}api/posts/${post.id}/likes")
                .delete(gson.toJson(post.id).toRequestBody(jsonType))
                .build()
        } else {
            Request.Builder()
                .url("${BASE_URL}api/posts/${post.id}/likes")
                .post(gson.toJson(post.id).toRequestBody(jsonType))
                .build()
        }
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val responseText = response.body?.string()
                        callback.onSuccess(gson.fromJson(responseText, Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }

                }

            })
    }

    override fun shareById(id: Long) {
        // TODO:
    }

    override fun removeById(id: Long, callback: PostRepository.NMediaCallback<Unit>) {
        val request = Request.Builder()
            .url("${BASE_URL}api/posts/$id")
            .delete(gson.toJson("").toRequestBody(jsonType))
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(Unit)
                }

            })
    }

    override fun save(post: Post, callback: PostRepository.NMediaCallback<Post>) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val responseText = response.body?.string()
                        callback.onSuccess(gson.fromJson(responseText, Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }

                }
            })
    }
}