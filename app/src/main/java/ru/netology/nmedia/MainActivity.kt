package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.extension.PostViewModel
import ru.netology.nmedia.extension.getFormatedNumber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postViewModel: PostViewModel by viewModels()
        postViewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                like.setImageResource(
                    if (post.likedByMe) R.drawable.baseline_favorite_24_red else R.drawable.baseline_favorite_border_24
                )
                likeCount.text = post.likeCount.getFormatedNumber()
                shareCount.text = post.shareCount.getFormatedNumber()
                visibilityCount.text = post.visibilityCount.getFormatedNumber()

                like.setOnClickListener {
                    postViewModel.like()
                }

                share.setOnClickListener {
                    postViewModel.share()
                }
            }
        }


    }
}