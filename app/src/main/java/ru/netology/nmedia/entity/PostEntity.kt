package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val likedByMe: Boolean = false,
    val published: String,
    val likes: Int = 0,
    val shareCount: Int = 0,
    val visibilityCount: Int = 0,
    val urlVideo: String? = null

) {
    fun toDto() = Post(
        id,
        author,
        content,
        likedByMe,
        published,
        likes,
        shareCount,
        visibilityCount,
        urlVideo
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.content,
                dto.likedByMe,
                dto.published,
                dto.likes,
                dto.shareCount,
                dto.visibilityCount,
                dto.urlVideo
            )
    }
}