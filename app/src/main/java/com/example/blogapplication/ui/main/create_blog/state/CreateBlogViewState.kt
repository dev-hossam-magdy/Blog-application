package com.example.blogapplication.ui.main.create_blog.state

import android.icu.text.CaseMap
import android.net.Uri

data class CreateBlogViewState(

    // createBlogFragment var
    var newBlogFields:NewBlogFields = NewBlogFields()
){
    data class NewBlogFields(
        var newBlogTitle:String? = null,
        var newBlogBody:String? = null,
        var newBlogImageUri:Uri? = null
    )
}