package com.example.blogapplication.ui

interface DataStateChangesListener {
    fun onDataStateChange(dataState: DataState<*>)

    fun hideKeyboard()
}

interface ToolBarExpandingListener{
    fun expandAppbar()
}