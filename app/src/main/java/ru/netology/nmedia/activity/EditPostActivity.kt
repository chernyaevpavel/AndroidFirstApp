package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.content.Intent.EXTRA_TEXT
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.save.setOnClickListener {
            val text = binding.content.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                setResult(RESULT_OK, Intent().apply {
                    putExtra(EXTRA_TEXT, text)
                })
            }
            finish()
        }

        binding.cancel.setOnClickListener() {
            setResult(RESULT_CANCELED)
            finish()
        }

        val content = intent.getStringExtra(EXTRA_TEXT)
        if (content != null) {
            binding.content.setText(content)
            binding.editInfo.visibility = View.VISIBLE
        }

    }
}

object EditPostContract : ActivityResultContract<String?, String?>() {
    override fun createIntent(context: Context, input: String?) =
        Intent(context, EditPostActivity::class.java).putExtra(EXTRA_TEXT, input)

    override fun parseResult(resultCode: Int, intent: Intent?) = intent?.getStringExtra(EXTRA_TEXT)

}