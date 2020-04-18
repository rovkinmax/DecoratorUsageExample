package com.rovkinmax.decoratorusageexample

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup

val Int.px2dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.px2dpF: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

inline fun ViewGroup.forEachIndexed(func: (index: Int, view: View) -> Unit) {
    for (i in 0 until childCount)
        func.invoke(i, getChildAt(i))
}