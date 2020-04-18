package com.rovkinmax.decoratorusageexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.rovkinmax.decoratorusageexample.recycler.AdapterUpdateCallback
import com.rovkinmax.decoratorusageexample.recycler.DiffMessageCallback
import com.rovkinmax.decoratorusageexample.recycler.InsertedFirstElementListener

class NoDecorationMessageAdapter(listener: InsertedFirstElementListener) : RecyclerView.Adapter<NoDecorationMessageAdapter.MessageHolder<ChatMessage>>() {

    private val updateCallback = AdapterUpdateCallback(this, listener)
    private val differ: AsyncListDiffer<ChatMessage> = AsyncListDiffer(
            updateCallback,
            AsyncDifferConfig.Builder(DiffMessageCallback())
                    .build()
    )

    fun setList(list: List<ChatMessage>) {
        differ.submitList(list.toList())
    }

    override fun getItemCount(): Int = differ.currentList.size
    private fun getItem(position: Int): ChatMessage = differ.currentList[position]

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder<ChatMessage> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.no_decor_item_message_image,
            R.layout.no_decor_item_message_image_my -> ImageMessageHolder(view)
            else -> TextMessageHolder(view)
        } as MessageHolder<ChatMessage>
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.isMy) {
            when (item) {
                is TextMessage -> R.layout.no_decor_item_message_text_my
                is ImageMessage -> R.layout.no_decor_item_message_image_my
            }
        } else {
            when (item) {
                is TextMessage -> R.layout.no_decor_item_message_text
                is ImageMessage -> R.layout.no_decor_item_message_image
            }
        }
    }

    override fun onBindViewHolder(holder: MessageHolder<ChatMessage>, position: Int) {
        holder.bind(getItem(position))
    }

    abstract class MessageHolder<M : ChatMessage>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(message: M)
    }


    class TextMessageHolder(itemView: View) : MessageHolder<TextMessage>(itemView) {
        private val textView = itemView.findViewById<TextView>(R.id.tvMessageText)
        private val timeView = itemView.findViewById<TextView>(R.id.tvTime)
        private val avatar: ImageView? = itemView.findViewById(R.id.avatar)

        override fun bind(message: TextMessage) {
            textView.text = message.text
            timeView.text = if (message.isMy) message.timeStampText else "${message.author.name}, ${message.timeStampText}"
            if (avatar != null) {
                Glide.with(avatar)
                        .load(message.author.avatar)
                        .error(R.drawable.ic_avatar)
                        .placeholder(R.drawable.ic_avatar)
                        .transform(CircleCrop())
                        .into(avatar)
            }
        }
    }

    class ImageMessageHolder(itemView: View) : MessageHolder<ImageMessage>(itemView) {
        private val imageView = itemView.findViewById<ImageView>(R.id.ivMessageImage)
        private val timeView = itemView.findViewById<TextView>(R.id.tvTime)
        private val avatar: ImageView? = itemView.findViewById(R.id.avatar)

        override fun bind(message: ImageMessage) {
            imageView.setImageResource(message.imageId)
            timeView.text = if (message.isMy) message.timeStampText else "${message.author.name}, ${message.timeStampText}"

            if (avatar != null) {
                Glide.with(avatar)
                        .load(message.author.avatar)
                        .error(R.drawable.ic_avatar)
                        .placeholder(R.drawable.ic_avatar)
                        .transform(CircleCrop())
                        .into(avatar)
            }
        }
    }
}