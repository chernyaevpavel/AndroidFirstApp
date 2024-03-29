package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.extension.getFormatedNumber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "29 мая в 18:36",
            shareCount = 996,
            visibilityCount = 500
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            if (post.likedByMe) {
                like.setImageResource(R.drawable.baseline_favorite_24_red)
            }
            likeCount.text = post.likeCount.getFormatedNumber()
            shareCount.text = post.shareCount.getFormatedNumber()
            visibilityCount.text = post.visibilityCount.getFormatedNumber()

            like.setOnClickListener {
                if (post.likedByMe) post.likeCount-- else post.likeCount++
                likeCount.text = post.likeCount.getFormatedNumber()
                post.likedByMe = !post.likedByMe
                like.setImageResource(
                    if (post.likedByMe) R.drawable.baseline_favorite_24_red else R.drawable.baseline_favorite_border_24
                )
            }

            share.setOnClickListener {
                post.shareCount++
                shareCount.text = post.shareCount.getFormatedNumber()
            }
        }
    }
}