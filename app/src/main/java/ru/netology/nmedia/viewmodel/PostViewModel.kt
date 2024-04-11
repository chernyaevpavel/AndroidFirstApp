package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

private val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = ""
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun changeContentAndSave(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content != text.trim()) {
                repository.save(it.copy(content = text))
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
}