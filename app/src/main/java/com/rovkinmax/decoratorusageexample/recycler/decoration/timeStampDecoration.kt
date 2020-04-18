package com.rovkinmax.decoratorusageexample.recycler.decoration

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.osome.stickydecorator.ConditionItemDecorator
import com.osome.stickydecorator.SimpleTextDrawable
import com.rovkinmax.decoratorusageexample.ChatMessage
import com.rovkinmax.decoratorusageexample.R
import com.rovkinmax.decoratorusageexample.px2dp
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

class TimeStampDecor(itemProvider: ItemProvider<ChatMessage>,
                     resources: Resources) : ConditionItemDecorator.SimpleDecor() {
    private val callback: Callback = TimeStampHelper(itemProvider)
    private var timeDrawable = buildTextDrawable(resources)
    private val viewBounds = Rect()

    @Suppress("DEPRECATION")
    private fun buildTextDrawable(resourceProvider: Resources): SimpleTextDrawable {
        return SimpleTextDrawable.Builder()
                .setTextColor(resourceProvider.getColor(R.color.main_black_transparent))
                .setPaddingSymmetric(0, 4.px2dp)
                .build()
    }

    override fun getConditionItemOffsets(parent: RecyclerView, rect: Rect, view: View, position: Int) {
        if (callback.isMy(position)) {
            rect.bottom += timeDrawable.height
        } else {
            rect.top += timeDrawable.height
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, child: View, position: Int, state: RecyclerView.State) {
        val viewBounds = getViewBounds(parent, child)
        timeDrawable.text = callback.getTimeStamp(position)

        if (callback.isMy(position)) {
            timeDrawable.setTopLeft(viewBounds.right - timeDrawable.textWidth, viewBounds.bottom)
        } else {
            timeDrawable.setTopLeft(
                    viewBounds.left,
                    viewBounds.top - timeDrawable.height
            )
        }

        timeDrawable.draw(c)
    }

    private fun getViewBounds(parent: ViewGroup, child: View): Rect {
        child.getDrawingRect(viewBounds)
        parent.offsetDescendantRectToMyCoords(child, viewBounds)
        return viewBounds
    }

    interface Callback {
        fun isMy(position: Int): Boolean
        fun getTimeStamp(position: Int): String
    }
}

class TimeStampHelper(private val itemProvider: ItemProvider<ChatMessage>) : TimeStampDecor.Callback {

    private val edited = ", edited"

    private fun getItem(position: Int) = itemProvider.getItem(position)

    override fun isMy(position: Int): Boolean {
        return itemProvider.getItem(position).isMy
    }

    override fun getTimeStamp(position: Int): String {
        val item = getItem(position)
        if (item.isMy)
            return item.timeStampText

        val timeStamp = item.timeStampText
        return "${item.author.name} $timeStamp".trim()
    }
}

class ChatTimeStampCondition(private val itemProvider: ItemProvider<ChatMessage>) : ConditionItemDecorator.Condition {
    companion object {
        private val DELTA_FIVE_MINUTES = TimeUnit.MINUTES.toMillis(5)
    }


    private val itemCount: Int
        get() = itemProvider.count


    private fun getItem(position: Int) = itemProvider.getItem(position)


    private fun isValidPosition(position: Int): Boolean {
        return position in 0 until itemCount && itemCount > 0
    }

    override fun isForDrawOver(position: Int): Boolean {
        if (isValidPosition(position).not())
            return false

        val item = getItem(position)

        if (item.isMy) {
            if (position == 0)
                return true
            val prevItem = getItem(position - 1)
            return isShowTimeStampForMessage(item, prevItem)
        }

        if (position == itemCount - 1)
            return true

        val nextItem = getItem(position + 1)
        return isShowTimeStampForMessage(item, nextItem)
    }


    private fun isShowTimeStampForMessage(current: ChatMessage, nearItem: ChatMessage): Boolean {
        if (current.author.id != nearItem.author.id)
            return true

        if ((current.date.time - nearItem.date.time).absoluteValue > DELTA_FIVE_MINUTES)
            return true
        return false
    }
}