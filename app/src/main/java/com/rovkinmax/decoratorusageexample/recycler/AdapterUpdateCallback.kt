package com.rovkinmax.decoratorusageexample.recycler

import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import java.util.logging.Level
import java.util.logging.Logger

typealias InsertedFirstElementListener = () -> Unit

class AdapterUpdateCallback(private val adapter: RecyclerView.Adapter<*>,
                            private val listener: InsertedFirstElementListener? = null) : ListUpdateCallback {

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter.notifyItemRangeChanged(position, count, payload)
        log("onChanged: position=$position count=$count")
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(fromPosition, toPosition)
        log("onMoved: from$fromPosition to=$toPosition")
        if (toPosition == 0)
            listener?.invoke()
    }

    override fun onInserted(position: Int, count: Int) {
        adapter.notifyItemRangeInserted(position, count)
        log("onInserted: position=$position count=$count")
        if (position == 0 && count == 1)
            listener?.invoke()

    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.notifyItemRangeRemoved(position, count)
        log("onRemoved: position=$position count=$count")
    }


    private fun log(msg: String) = Logger.getGlobal().log(Level.INFO, msg)
}