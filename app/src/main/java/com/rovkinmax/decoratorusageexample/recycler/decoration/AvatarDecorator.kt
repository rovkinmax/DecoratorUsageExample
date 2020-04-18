package com.rovkinmax.decoratorusageexample.recycler.decoration

import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.LruCache
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.osome.stickydecorator.ConditionItemDecorator
import com.osome.stickydecorator.SimpleTextDrawable
import com.rovkinmax.decoratorusageexample.ChatMessage
import com.rovkinmax.decoratorusageexample.R
import com.rovkinmax.decoratorusageexample.forEachIndexed
import com.rovkinmax.decoratorusageexample.px2dp


class AvatarDecorator(private val callback: Callback) : RecyclerView.ItemDecoration() {

    private var timeDrawable = buildTextDrawable()
    private val avatarSize = 40.px2dp
    private val avatarRightMargin = 4.px2dp
    private var avatarHelper: AvatarDrawer? = null
    private val avatarPosition = Point()

    private fun buildTextDrawable(): SimpleTextDrawable {
        return SimpleTextDrawable.Builder()
                .build()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (state.itemCount <= 0)
            return

        val position = parent.getChildAdapterPosition(view)

        if (callback.isRequireHorizontalSpace(position)) {
            outRect.left += avatarSize + avatarRightMargin
        }

    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        if (state.itemCount <= 0)
            return

        if (avatarHelper == null)
            avatarHelper = AvatarDrawer(parent, avatarSize)

        parent.forEachIndexed { _, view ->
            val position = parent.getChildAdapterPosition(view)

            if (callback.isShowAvatar(position))
                drawAvatar(position, view, c)
        }
    }

    private fun drawAvatar(position: Int, view: View, c: Canvas) {
        val url = callback.getAvatarUrl(position)
        val x = view.left - avatarSize - avatarRightMargin
        val y = view.top - timeDrawable.height
        avatarPosition.set(x, y)
        avatarHelper?.showAvatar(url, avatarPosition, c)
    }

    interface Callback {

        fun isRequireHorizontalSpace(position: Int): Boolean

        fun isShowAvatar(position: Int): Boolean

        fun getAvatarUrl(position: Int): String
    }

    private class AvatarDrawer(private val parent: RecyclerView, private val avatarSize: Int) {

        private val cacheSize = (Runtime.getRuntime().maxMemory() / (1024 * 8)).toInt()
        private val cache = LruCache<String, Avatar>(cacheSize)

        fun showAvatar(url: String, position: Point, c: Canvas) {
            val avatar = cache[url]
            if (avatar != null)
                drawAvatar(avatar, position, c)

            if (avatar == null || avatar.isDefault)
                Glide.with(parent)
                        .load(url)
                        .override(avatarSize, avatarSize)
                        .error(R.drawable.ic_avatar)
                        .placeholder(R.drawable.ic_avatar)
                        .override(avatarSize, avatarSize)
                        .transform(CircleCrop())
                        .into(object : CustomTarget<Drawable>(avatarSize, avatarSize) {
                            override fun onLoadStarted(placeholder: Drawable?) {
                                putInToCache(url, placeholder, isDefault = true)
                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                putInToCache(url, errorDrawable, isDefault = true)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {}

                            override fun onResourceReady(
                                    resource: Drawable,
                                    transition: Transition<in Drawable>?
                            ) {
                                putInToCache(url, resource, isDefault = false)
                            }
                        })
        }

        private fun putInToCache(url: String, icon: Drawable?, isDefault: Boolean) {
            if (icon == null) return
            val avatar = Avatar(icon, isDefault)
            cache.put(url, avatar)
            parent.invalidateItemDecorations()
        }

        private fun drawAvatar(avatar: Avatar, position: Point, c: Canvas) {
            avatar.icon.setBounds(
                    position.x,
                    position.y,
                    position.x + avatarSize,
                    position.y + avatarSize
            )
            avatar.icon.draw(c)
        }
    }

    private class Avatar(
            val icon: Drawable,
            val isDefault: Boolean
    )
}

class ChatAvatarHelper(private val itemProvider: ItemProvider<ChatMessage>) : AvatarDecorator.Callback, ConditionItemDecorator.Condition {

    private val timeStampHelper = ChatTimeStampCondition(itemProvider)

    private val itemCount: Int
        get() = itemProvider.count


    private fun getItem(position: Int) = itemProvider.getItem(position)

    override fun isRequireHorizontalSpace(position: Int): Boolean {
        if (isValidPosition(position).not())
            return false

        return getItem(position).isMy.not()
    }

    override fun getAvatarUrl(position: Int): String {
        if (isValidPosition(position).not())
            return ""

        val item = getItem(position)
        return item.author.avatar
    }

    override fun isShowAvatar(position: Int): Boolean {
        val isShowTimeStamp = isForDrawOver(position)
        if (isShowTimeStamp.not())
            return false

        val item = getItem(position)

        if (item.isMy)
            return false

        return true
    }

    override fun isForDrawOver(position: Int): Boolean {
        return timeStampHelper.isForDrawOver(position)
    }

    private fun isValidPosition(position: Int): Boolean {
        return position in 0 until itemCount && itemCount > 0
    }
}