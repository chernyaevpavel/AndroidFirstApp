package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostFragment.Companion.longArg
import ru.netology.nmedia.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel


class PostFragment : Fragment() {
    private val postViewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CardPostBinding.inflate(
            inflater,
            container,
            false
        )

        postViewModel.data.observe(viewLifecycleOwner) { model ->
            val posts = model.posts
            val post = posts.find { it.id == arguments?.longArg } ?: return@observe
            val holder = PostViewHolder(binding, object : OnInteractionListener {
                override fun onLike(post: Post) {
                    postViewModel.likeById(post)
                }

                override fun onShare(post: Post) {
                    postViewModel.shareById(post.id)
                }

                override fun onRemove(post: Post) {
                    postViewModel.removeById(post.id)
                    findNavController().navigateUp()
                }

                override fun onEdit(post: Post) {
                    postViewModel.edit(post)
                    findNavController().navigate(
                        R.id.action_postFragment_to_editPostFragment,
                        Bundle().apply {
                            textArg = post.content
                        })
                }

                override fun onPlay(post: Post) {
                    startActivity(postViewModel.prepareVideoIntent(post))
                }

                override fun onOpenCardPost(post: Post) {
                }
            })
            holder.bind(post)
        }
        return binding.root
    }

}