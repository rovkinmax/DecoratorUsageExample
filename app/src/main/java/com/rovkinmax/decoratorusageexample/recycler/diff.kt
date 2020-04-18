package com.rovkinmax.decoratorusageexample.recycler

import androidx.recyclerview.widget.DiffUtil
import com.rovkinmax.decoratorusageexample.ChatMessage

class DiffMessageCallback : DiffUtil.ItemCallback<ChatMessage>() {
    override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem == newItem
    }
}