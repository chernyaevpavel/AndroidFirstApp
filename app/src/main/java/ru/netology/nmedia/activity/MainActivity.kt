package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postViewModel: PostViewModel by viewModels()
        val editPostLauncher = registerForActivityResult(EditPostContract) {
            if (it == null) {
                postViewModel.setEmptyPost()
                return@registerForActivityResult
            }
            postViewModel.changeContentAndSave(it)
            postViewModel.setEmptyPost()
        }

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                postViewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                postViewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                postViewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                postViewModel.edit(post)
            }

            override fun onPlay(post: Post) {
                startActivity(postViewModel.prepareVideoIntent(post))
            }
        }
        )

        binding.list.adapter = adapter

        postViewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) binding.list.smoothScrollToPosition(0)
            }
        }

        binding.addNewPost.setOnClickListener {
            editPostLauncher.launch(null)
        }

        postViewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            editPostLauncher.launch(post.content)
        }
    }
}