package com.example.blogapplication.ui.main.blog

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager

import com.example.blogapplication.R
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.ui.convertLongToStringDate
import kotlinx.android.synthetic.main.fragment_view_blog.*
import javax.inject.Inject


class ViewBlogFragment : BaseBlogFragment() {
    override val TAG: String
        get() = "ViewBlogFragment"
    @Inject
    lateinit var requestManager: RequestManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                it.viewBlogFields?.let { viewBlogFields ->
                    viewBlogFields.blogPost?.let { blogPost ->
                        setBlogPostProperties(blogPost)

                    }

                }

            }
        })
    }

    private fun setBlogPostProperties(blogPost: BlogPost) {
        requestManager
            .load(blogPost.image)
            .into(blog_image_item)
        blog_title_item.setText(blogPost.title)
        blog_author_item.setText(blogPost.username)
        blog_update_date_item.setText(blogPost.dateUpdate.convertLongToStringDate())
        blog_body.setText(blogPost.body)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
        val isAuthorOfBlog =  true
        if (isAuthorOfBlog)
            inflater.inflate(R.menu.edit_view_menu,menu)

        //
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val isAuthorOfBlog =  true
        if (isAuthorOfBlog && item.itemId == R.id.edit){
            navigateToDestination(R.id.action_viewBlogFragment_to_updateBlogFragment)

            return true
        }
        return super.onOptionsItemSelected(item)
    }



}
