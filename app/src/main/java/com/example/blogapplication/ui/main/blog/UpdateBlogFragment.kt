package com.example.blogapplication.ui.main.blog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.blogapplication.R

/**
 * A simple [Fragment] subclass.
 */
class UpdateBlogFragment : BaseBlogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_blog, container, false)
    }

}
