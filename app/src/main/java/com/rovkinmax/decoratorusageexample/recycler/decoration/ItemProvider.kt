package com.rovkinmax.decoratorusageexample.recycler.decoration

interface ItemProvider<T> {
    fun getItem(position: Int): T
    val count: Int
}