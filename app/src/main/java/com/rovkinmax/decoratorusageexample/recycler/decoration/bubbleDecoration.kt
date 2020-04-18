package com.rovkinmax.decoratorusageexample.recycler.decoration

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rovkinmax.decoratorusageexample.ChatMessage
import com.rovkinmax.decoratorusageexample.R
import com.rovkinmax.decoratorusageexample.forEachIndexed

class BubbleDecorator(
        resources: Resources,
        private val itemProvider: ItemProvider<ChatMessage>,
        private val condition: ChatTimeStampCondition
) : RecyclerView.ItemDecoration() {

    private val justBubble = resources.getDrawable(R.drawable.chat_message_bg)
    private val myBubble = resources.getDrawable(R.drawable.chat_message_bg_my)
    private val agentColor = resources.getColor(R.color.colorAgent)
    private val myColor = resources.getColor(R.color.colorMy)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (state.itemCount <= 0)
            return
        parent.forEachIndexed { _, view ->
            val position = parent.getChildAdapterPosition(view)
            if (position >= 0) {
                if (itemProvider.getItem(position).isMy)
                    drawMyBubble(view, position, c)
                else if (view.tag == "true") drawAgentBubble(view, c)
            }
        }
    }

    private fun drawAgentBubble(view: View, c: Canvas) {
        justBubble.setTint(agentColor)
        drawBubbleDrawable(view, view, justBubble, c)
    }

    private fun drawMyBubble(view: View, position: Int, c: Canvas) {
        val group = view as? ViewGroup
        group?.forEachIndexed { _, child ->
            if (child.tag == "true") {
                val drawable = if (isShowMyBubble(position)) myBubble else justBubble
                drawable?.setTint(myColor)
                drawBubbleDrawable(group, child, drawable, c)
            }
        }
    }

    private fun isShowMyBubble(position: Int): Boolean {
        return condition.isForDrawOver(position)
    }

    private fun drawBubbleDrawable(parent: View, view: View, drawable: Drawable?, c: Canvas) {
        val right = parent.right
        val left = right - (view.right - view.left)
        val bottom = parent.bottom
        val top = bottom - (view.bottom - view.top)

        drawable?.setBounds(left, top, right, bottom)
        drawable?.draw(c)
    }
}