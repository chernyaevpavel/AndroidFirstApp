package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.utils.LongArg
import ru.netology.nmedia.utils.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class EditPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
        var Bundle.longArg: Long by LongArg
    }

    private val postViewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            postViewModel.setEmptyPost()
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditPostBinding.inflate(
            inflater, container, false
        )
        binding.save.setOnClickListener {
            postViewModel.changeContentAndSave(binding.content.text.toString())
        }

        binding.cancel.setOnClickListener() {
            postViewModel.setEmptyPost()
            findNavController().navigateUp()
        }

        arguments?.textArg?.let {
            val content = it
            binding.content.setText(content)
            binding.editInfo.visibility = View.VISIBLE
        }
        postViewModel.postCreated.observe(viewLifecycleOwner) {
            postViewModel.setEmptyPost()
            findNavController().navigateUp()
            postViewModel.load()
        }
        return binding.root
    }

}