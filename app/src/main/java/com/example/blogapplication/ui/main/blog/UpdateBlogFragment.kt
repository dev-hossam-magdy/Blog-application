package com.example.blogapplication.ui.main.blog

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.RequestManager

import com.example.blogapplication.R
import com.example.blogapplication.ui.main.blog.state.BlogPostStateEvent
import kotlinx.android.synthetic.main.fragment_update_blog.*
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class UpdateBlogFragment : BaseBlogFragment() {
    override val TAG: String
        get() = "UpdateBlogFragment"

    @Inject
    lateinit var requestManager: RequestManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
    }


    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState->
            stateChangeListener.onDataStateChange(dataState)
            dataState.data?.let {  event ->
                event.getContentIfNotHandled()?.let { blogPostViewState ->
                    blogPostViewState.viewBlogFields.blogPost?.let { blogPost ->
                        // on blog post updated success
                    }
                }
            }

        })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.updateBlogFields.let { updateBlogFields ->
                setBlogProperties(
                    updateBlogFields.updatedBlogTitle ,
                    updateBlogFields.updatedBlogBody,
                    updateBlogFields.updatedImageUri
                )
            }

        })
    }

    private fun setBlogProperties(
        updatedBlogTitle: String?,
        updatedBlogBody: String?,
        updatedImageUri: Uri?
    ) {
        requestManager.load(updatedImageUri)
            .into(blog_image_item)
        blog_title_item.setText(updatedBlogTitle)
        blog_body.setText(updatedBlogBody)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.updete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem)=
        if (item.itemId == R.id.check_menu){
            saveChanges()
            true
        }
        else
         super.onOptionsItemSelected(item)

    private fun saveChanges() {
        var multipartBody: MultipartBody.Part? = null
        viewModel.setStateEvent(
            BlogPostStateEvent.UpdatedBlogPostEvent(
                title = blog_title_item.text.toString(),
                body = blog_body.text.toString(),
                image = multipartBody
            )
        )
    }


}
