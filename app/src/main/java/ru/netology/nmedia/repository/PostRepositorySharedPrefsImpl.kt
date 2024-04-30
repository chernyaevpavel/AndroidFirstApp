package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import java.text.SimpleDateFormat
import java.util.Date

class PostRepositorySharedPrefsImpl(
    context: Context
) : PostRepository {
    companion object {
        private const val KEY = "posts"
    }

    private val gson = Gson()
    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var nextId = 1L
    private var posts = emptyList<Post>()
        private set(value) {
            field = value
            sync()
        }
    private var defaultPosts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "29 мая в 18:36",
            shareCount = 996,
            visibilityCount = 500
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это другой пост! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "6 июня в 07:15",
            shareCount = 10847,
            visibilityCount = 3,
            urlVideo = "https://www.youtube.com/watch?v=XqZsoesa55w"
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это третий пост! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "6 июня в 07:15",
            shareCount = 10847,
            visibilityCount = 3
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это четвертый пост! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "6 июня в 07:15",
            shareCount = 10847,
            likeCount = 999,
            visibilityCount = 3,
            urlVideo = "https://www.youtube.com/watch?v=6R9HOhG2CWU"
        )
    )
    private val data = MutableLiveData(posts)

    init {
        prefs.getString(KEY, null)?.let {
            posts = gson.fromJson(it, typeToken)
            nextId = posts.maxOf { it.id } + 1
        } ?: run {
            posts = defaultPosts
        }
        data.value = posts
    }

    private fun sync() {
        with(prefs.edit()) {
            putString(KEY, gson.toJson(posts))
            apply()
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) {
                it
            } else {
                val likeCount = if (!it.likedByMe) it.likeCount + 1 else it.likeCount - 1
                it.copy(
                    likedByMe = !it.likedByMe,
                    likeCount = likeCount
                )
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) {
                it
            } else {
                it.copy(shareCount = it.shareCount + 1)
            }
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        val sdf = SimpleDateFormat("dd.MM.yy hh:mm")
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    published = sdf.format(Date())
                )
            ) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id == post.id) it.copy(content = post.content) else it
        }
        data.value = posts
    }
}