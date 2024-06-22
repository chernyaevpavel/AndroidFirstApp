package ru.netology.nmedia.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl
import ru.netology.nmedia.utils.SingleLiveEvent


private val empty = Post(
    id = 0, author = "", content = "", published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryRoomImpl()
    private val _data = MutableLiveData<FeedModel>()
    val data: LiveData<FeedModel>
        get() = _data
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
    val edited = MutableLiveData(empty)

    init {
        load()
    }

    fun load() {
        _data.postValue(FeedModel(loading = true))
        repository.getAll(
            object : PostRepository.NMediaCallback<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _data.postValue(FeedModel(posts = data, empty = data.isEmpty()))
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
    }


    fun likeById(post: Post) {
        repository.likeById(post, object : PostRepository.NMediaCallback<Post> {
            override fun onSuccess(data: Post) {
                val model = _data.value ?: return
                _data.postValue(
                    model.copy(posts = model.posts.map {
                        if (it.id == data.id) {
                            data
                        } else {
                            it
                        }
                    })
                )
            }

            override fun onError(e: java.lang.Exception) {
                _data.postValue(FeedModel(error = true))
            }

        })
    }

    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) {
        repository.removeById(id, object : PostRepository.NMediaCallback<Unit> {
            override fun onSuccess(data: Unit) {
                load()
            }

            override fun onError(e: java.lang.Exception) {
                _data.postValue(FeedModel(error = true))
            }

        })
    }
    fun changeContentAndSave(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content != text.trim()) {
                repository.save(
                    it.copy(content = text),
                    object : PostRepository.NMediaCallback<Post> {
                        override fun onSuccess(data: Post) {
                            this@PostViewModel._postCreated.postValue(Unit)
                        }

                        override fun onError(e: java.lang.Exception) {
                            _data.postValue(FeedModel(error = true))
                        }
                    })
            }
        }
        edited.value = empty
    }
    fun edit(post: Post) {
        edited.value = post
    }
    fun setEmptyPost() {
        edited.value = empty
    }

    fun prepareVideoIntent(post: Post): Intent {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            type = "audio/*"
            data = Uri.parse(post.urlVideo)
        }
        return Intent.createChooser(intent, "video")
    }
}