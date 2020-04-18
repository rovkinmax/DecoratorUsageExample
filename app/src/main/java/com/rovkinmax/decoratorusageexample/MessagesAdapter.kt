package com.rovkinmax.decoratorusageexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.rovkinmax.decoratorusageexample.recycler.AdapterUpdateCallback
import com.rovkinmax.decoratorusageexample.recycler.DiffMessageCallback
import com.rovkinmax.decoratorusageexample.recycler.InsertedFirstElementListener
import com.rovkinmax.decoratorusageexample.recycler.decoration.ItemProvider

class MessagesAdapter(listener: InsertedFirstElementListener) : RecyclerView.Adapter<MessagesAdapter.MessageHolder<ChatMessage>>(), ItemProvider<ChatMessage> {

    private val updateCallback = AdapterUpdateCallback(this, listener)
    private val differ: AsyncListDiffer<ChatMessage> = AsyncListDiffer(
            updateCallback,
            AsyncDifferConfig.Builder(DiffMessageCallback())
                    .build()
    )

    override val count: Int
        get() = itemCount

    fun setList(list: List<ChatMessage>) {
        differ.submitList(list.toList())
    }

    override fun getItemCount(): Int = differ.currentList.size
    override fun getItem(position: Int): ChatMessage = differ.currentList[position]

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder<ChatMessage> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_message_image,
            R.layout.item_message_image_my -> ImageMessageHolder(view)
            else -> TextMessageHolder(view)
        } as MessageHolder<ChatMessage>
    }

    override fun onBindViewHolder(holder: MessageHolder<ChatMessage>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.isMy) {
            when (item) {
                is TextMessage -> R.layout.item_message_text_my
                is ImageMessage -> R.layout.item_message_image_my
            }
        } else {
            when (item) {
                is TextMessage -> R.layout.item_message_text
                is ImageMessage -> R.layout.item_message_image
            }
        }
    }

    abstract class MessageHolder<M : ChatMessage>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(message: M)
    }

    class TextMessageHolder(itemView: View) : MessageHolder<TextMessage>(itemView) {
        private val textView = itemView.findViewById<TextView>(R.id.tvMessageText)

        override fun bind(message: TextMessage) {
            textView.text = message.text
        }
    }

    class ImageMessageHolder(itemView: View) : MessageHolder<ImageMessage>(itemView) {
        private val imageView = itemView.findViewById<ImageView>(R.id.ivMessageImage)

        override fun bind(message: ImageMessage) {
            imageView.setImageResource(message.imageId)
        }
    }
}