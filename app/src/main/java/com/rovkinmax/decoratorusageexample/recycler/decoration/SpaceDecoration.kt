package com.rovkinmax.decoratorusageexample.recycler.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceDecoration(
        private val left: Int,
        private val right: Int,
        private val vertical: Int,
        private val callback: Callback = DefaultCallback()
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (callback.isRequireVerticalSpace(position))
            outRect.bottom += vertical

        if (callback.isRequireHorizontalSpace(position)) {
            outRect.left += left
            outRect.right += right
        }
    }

    private class DefaultCallback : Callback {
        override fun isRequireHorizontalSpace(position: Int): Boolean = true
        override fun isRequireVerticalSpace(position: Int): Boolean = true
    }

    interface Callback {
        fun isRequireHorizontalSpace(position: Int): Boolean
        fun isRequireVerticalSpace(position: Int): Boolean
    }
}