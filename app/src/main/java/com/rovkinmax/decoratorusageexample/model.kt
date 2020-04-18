package com.rovkinmax.decoratorusageexample

import androidx.annotation.DrawableRes
import java.util.*

data class Author(
        val id: Long,
        val name: String,
        val avatar: String
)

sealed class ChatMessage {
    abstract val id: Long
    abstract val author: Author
    abstract val date: Date
    abstract val timeStampText: String
    abstract val dateText: String
    abstract val isMy: Boolean
}

data class TextMessage(
        override val id: Long,
        override val author: Author,
        override val date: Date,
        override val timeStampText: String,
        override val dateText: String,
        override val isMy: Boolean,
        val text: String
) : ChatMessage()

data class ImageMessage(
        override val id: Long,
        override val author: Author,
        override val date: Date,
        override val timeStampText: String,
        override val dateText: String,
        override val isMy: Boolean,
        @DrawableRes val imageId: Int
) : ChatMessage()