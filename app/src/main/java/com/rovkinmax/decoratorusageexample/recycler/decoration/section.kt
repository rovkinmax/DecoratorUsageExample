package com.rovkinmax.decoratorusageexample.recycler.decoration

import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.osome.stickydecorator.ConditionItemDecorator
import com.osome.stickydecorator.SimpleTextDrawable
import com.osome.stickydecorator.VerticalStickyDrawableDecor
import com.rovkinmax.decoratorusageexample.ChatMessage
import com.rovkinmax.decoratorusageexample.R
import com.rovkinmax.decoratorusageexample.px2dp

class DateSectionDecor(
        private val resources: Resources,
        private val callback: Callback
) : VerticalStickyDrawableDecor(true) {

    private val section = buildDrawable()
    private val header = buildDrawable()
    private val margins = 8.px2dp

    private fun buildDrawable(): SimpleTextDrawable {
        return SimpleTextDrawable.Builder()
                .setPaddingSymmetricDp(6, 4)
                .setBackgroundCornerRadiusDp(10)
                .setTextColor(resources.getColor(R.color.gray))
                .setBackgroundColor(resources.getColor(R.color.main_black_transparent))
                .build()
    }

    override fun getHeaderDrawable(position: Int, headerBounds: Rect): Drawable {
        return header
    }

    override fun getHeaderBounds(
            parent: RecyclerView,
            headerBottom: Int,
            itemPosition: Int,
            viewBounds: Rect,
            decoratedBounds: Rect
    ): Rect {
        header.text = callback.getSectionText(itemPosition)
        val bounds =
                super.getHeaderBounds(parent, headerBottom, itemPosition, viewBounds, decoratedBounds)
        header.setTopCenter(parent.width / 2, bounds.top)
        return header.bounds
    }

    override fun getSectionHeight(position: Int): Int = section.height

    override fun getSectionDrawable(position: Int, sectionBounds: Rect, child: View): Drawable {
        return section
    }

    override fun getSectionBounds(
            parent: RecyclerView,
            position: Int,
            viewBounds: Rect,
            decoratedBounds: Rect
    ): Rect {
        section.text = callback.getSectionText(position)
        val bounds = super.getSectionBounds(parent, position, viewBounds, decoratedBounds)
        section.setTopCenter(parent.width / 2, bounds.top)
        return section.bounds
    }

    override fun getSectionMarginBottom(): Int = margins

    override fun getSectionMarginTop(): Int = margins

    interface Callback {
        fun getSectionText(position: Int): String
    }
}

class ChatDateSeparatorHelper(private val itemProvider: ItemProvider<ChatMessage>) :
        DateSectionDecor.Callback, ConditionItemDecorator.Condition {
    private val itemCount: Int
        get() = itemProvider.count

    private fun getItem(position: Int) = itemProvider.getItem(position)

    override fun isForDrawOver(position: Int): Boolean {
        if (isValidPosition(position).not())
            return false

        val current = getSectionText(position)
        val next = getSectionText(position + 1)
        return current != next
    }

    override fun getSectionText(position: Int): String {
        if (isValidPosition(position).not())
            return ""

        val item = getItem(position)
        return item.dateText
    }


    private fun isValidPosition(position: Int): Boolean {
        return position in 0 until itemCount && itemCount > 0
    }
}