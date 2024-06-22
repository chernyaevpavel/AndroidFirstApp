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
import kotlin.concurrent.thread


private val empty = Post(
    id = 0, author = "", content = "", published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    //    private val repository: PostRepository = PostRepositorySharedPrefsImpl(application)
    //    private val repository: PostRepository = PostRepositoryFileImpl(application)
    //    private val repository: PostRepository = PostRepositorySQLiteImpl(
    //        AppDb.getInstance(application).postDao
    //    )
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
        thread {
            _data.postValue(FeedModel(loading = true))
            _data.postValue(
                try {
                    val posts = repository.getAll()
                    FeedModel(posts = posts, empty = posts.isEmpty())
                } catch (e: Exception) {
                    FeedModel(error = true)
                }
            )
        }
    }

    fun likeById(post: Post) {
        thread {
            val post = repository.likeById(post)
            val model = _data.value ?: return@thread
            _data.postValue(model.copy(posts = model.posts.map {
                if (it.id == post.id) {
                    post
                } else {
                    it
                }
            }))
        }
    }

    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) {
        thread {
            repository.removeById(id)
            //2 раза дергать сервер "такоесебе", но поскольку сервер немного "туповат"
            // и всегда присылает 200 статус, даже на несуществующие посты,
            // то это единственное решение корректно отобразить список постов
            load()
        }
    }
    fun changeContentAndSave(content: String) {
        thread {
            edited.value?.let {
                val text = content.trim()
                if (it.content != text.trim()) {
                    repository.save(it.copy(content = text))
                    _postCreated.postValue(Unit)
                }
            }
            edited.postValue(empty)
        }
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