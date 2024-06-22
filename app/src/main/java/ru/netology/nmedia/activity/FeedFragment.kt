package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostFragment.Companion.longArg
import ru.netology.nmedia.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    private val postViewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )
        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                postViewModel.likeById(post)
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

            override fun onOpenCardPost(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_postFragment,
                    Bundle().apply {
                        longArg = post.id
                    })
            }
        }
        )
        binding.list.adapter = adapter

        postViewModel.data.observe(viewLifecycleOwner) {model ->
            binding.errorGroup.isVisible = model.error
            binding.emptyState.isVisible = model.empty
            binding.progress.isVisible = model.loading
            adapter.submitList(model.posts)
        }



        binding.addNewPost.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_editPostFragment)
        }

        postViewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id == 0L) {
                return@observe
            }
            findNavController().navigate(
                R.id.action_feedFragment_to_editPostFragment,
                Bundle().apply {
                    textArg = post.content
                })
        }
        binding.retry.setOnClickListener {
            postViewModel.load()
        }

        return binding.root
    }
}