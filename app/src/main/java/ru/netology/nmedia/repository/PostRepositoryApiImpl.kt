package ru.netology.nmedia.repository

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.R
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dto.Post

class PostRepositoryApiImpl(private val context: Context) : PostRepository {
    override fun getAll(callback: PostRepository.NMediaCallback<List<Post>>) {
        ApiService.service.getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(context.getString(R.string.failed_refresh_news)))
                        return
                    }
                    val body: List<Post> = response.body() ?: throw RuntimeException(
                        context.getString(
                            R.string.body_is_null
                        )
                    )
                    try {
                        callback.onSuccess(body)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }


    override fun likeById(post: Post, callback: PostRepository.NMediaCallback<Post>) {
        if (post.likedByMe) {
            ApiService.service.unlikeById(post.id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(context.getString(R.string.failed_refresh_news)))
                            return
                        }
                        val body: Post = response.body() ?: throw RuntimeException(
                            context.getString(
                                R.string.body_is_null
                            )
                        )
                        try {
                            callback.onSuccess(body)
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(Exception(t))
                    }
                })
        } else {
            ApiService.service.likeById(post.id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(context.getString(R.string.failed_refresh_news)))
                            return
                        }
                        val body: Post = response.body() ?: throw RuntimeException(
                            context.getString(
                                R.string.body_is_null
                            )
                        )
                        try {
                            callback.onSuccess(body)
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(Exception(t))
                    }
                })
        }
    }

    override fun shareById(id: Long) {
        // TODO:
    }

    override fun removeById(id: Long, callback: PostRepository.NMediaCallback<Unit>) {
        ApiService.service.removeById(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(context.getString(R.string.faile_delete_post)))
                        return
                    }
                    try {
                        callback.onSuccess(Unit)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }

    override fun save(post: Post, callback: PostRepository.NMediaCallback<Post>) {
        ApiService.service.save(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(context.getString(R.string.failed_save_post)))
                        return
                    }
                    val body: Post = response.body()
                        ?: throw RuntimeException(context.getString(R.string.body_is_null))
                    try {
                        callback.onSuccess(body)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }
}