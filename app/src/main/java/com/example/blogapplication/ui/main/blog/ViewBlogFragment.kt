package com.example.blogapplication.ui.main.blog

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager

import com.example.blogapplication.R
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.ui.AreYouSureCallback
import com.example.blogapplication.ui.UIMessage
import com.example.blogapplication.ui.UIMessageType
import com.example.blogapplication.ui.convertLongToStringDate
import com.example.blogapplication.ui.main.blog.state.BlogPostStateEvent
import com.example.blogapplication.ui.main.blog.viewmodels.getBlogPost
import com.example.blogapplication.ui.main.blog.viewmodels.getIsAuthorOfBlogPost
import com.example.blogapplication.ui.main.blog.viewmodels.setIsAuthorOfBlogPost
import com.example.blogapplication.ui.main.blog.viewmodels.setUpdatedBlogFields
import com.example.blogapplication.util.SuccessHandling
import kotlinx.android.synthetic.main.fragment_view_blog.*
import java.lang.Exception
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
        checkIfAuthorOfBlogPost()

        setActionsTiWidgets()
    }

    private fun setActionsTiWidgets() {
        delete_button.setOnClickListener {
            confirmDeleteRequest()
        }
    }

    private fun confirmDeleteRequest() {
        uiCommunicationListener.onUIMessageReceived(UIMessage(
            message = getString(R.string.are_you_sure_delete),
            uiMessageType = UIMessageType.AreYouSureDialog(
                object : AreYouSureCallback {
                    override fun proceed() {
                        deleteBlogPost()
                    }

                    override fun cancel() {
                        //ignore
                    }
                }
            )
        ))
    }

    private fun deleteBlogPost() {
        viewModel.setStateEvent(BlogPostStateEvent.DeleteBlogPostEvent())
    }

    private fun checkIfAuthorOfBlogPost() {
        viewModel.setIsAuthorOfBlogPost(false)

        viewModel.setStateEvent(BlogPostStateEvent.CheckAuthorOfBlogPost())
    }

    private fun subscribeObserver() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)

            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { blogPostViewState ->
                    viewModel.setIsAuthorOfBlogPost(blogPostViewState.viewBlogFields.isAuthorOfBlogPost)

                }
                dataState.response?.peekContent()?.let { response ->
                    response.message?.let {
                        if (it.equals(SuccessHandling.SUCCESS_BLOG_DELETED)) {
                            viewModel.removeDeletedBlogPost()
                            findNavController().popBackStack()
                        }
                    }

                }

            }

        })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                it.viewBlogFields.let { viewBlogFields ->
                    viewBlogFields.blogPost?.let { blogPost ->
                        setBlogPostProperties(blogPost)
                    }
                    if (!viewBlogFields.isAuthorOfBlogPost)
                        adaptsViewToAuthorMode()
                }
            }
        })
    }

    private fun adaptsViewToAuthorMode() {
        activity?.invalidateOptionsMenu()
        delete_button.visibility = View.VISIBLE
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
        if (!viewModel.getIsAuthorOfBlogPost())
            inflater.inflate(R.menu.edit_view_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!viewModel.getIsAuthorOfBlogPost() && item.itemId == R.id.edit) {
            navigateToDestination(R.id.action_viewBlogFragment_to_updateBlogFragment)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun navigateToDestination(destination: Int) {
        try {
            val blogPost = viewModel.getBlogPost()
            viewModel.setUpdatedBlogFields(
                title = blogPost.title,
                body = blogPost.body,
                imageUri = blogPost.image.toUri()
            )
            super.navigateToDestination(destination)
        } catch (e: Exception) {
            Log.e(TAG, "error in navigation ${e.message}")
        }

    }

}
