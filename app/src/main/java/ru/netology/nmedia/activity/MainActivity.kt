package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.utils.AndroidUtils.focusAndShowKeyboard
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postViewModel: PostViewModel by viewModels()
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
        }
        )

        binding.list.adapter = adapter
        postViewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) binding.list.smoothScrollToPosition(0)
            }
        }

        postViewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.content) {
                setText(post.content)
                binding.editGroup.visibility = View.VISIBLE
                this.focusAndShowKeyboard()

            }
            binding.editText.text = post.content
        }

        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.not_empty_content,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                postViewModel.changeContentAndSave(text.toString())
                finishEditing(binding)
            }
        }
        binding.editCancel.setOnClickListener {
            postViewModel.setEmptyPost()
            finishEditing(binding)
        }
    }
    private fun finishEditing(binding: ActivityMainBinding) {
        with(binding.content) {
            setText("")
            clearFocus()
            AndroidUtils.hideKeyboard(this)
        }
        binding.editGroup.visibility = View.GONE
    }
}