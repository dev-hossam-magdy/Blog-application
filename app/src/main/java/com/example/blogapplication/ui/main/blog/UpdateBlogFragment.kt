package com.example.blogapplication.ui.main.blog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.blogapplication.R
import com.example.blogapplication.ui.main.blog.state.BlogPostStateEvent
import com.example.blogapplication.ui.main.blog.viewmodels.getUpdatedBlogUri
import com.example.blogapplication.ui.main.blog.viewmodels.onBlogPostUpdateSuccess
import com.example.blogapplication.ui.main.blog.viewmodels.setUpdatedBlogFields
import com.example.blogapplication.ui.main.create_blog.state.CreateBlogStateEvent
import com.example.blogapplication.util.Constants
import com.example.blogapplication.util.ErrorHandling
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_create_blog.*
import kotlinx.android.synthetic.main.fragment_update_blog.*
import kotlinx.android.synthetic.main.fragment_update_blog.blog_body
import kotlinx.android.synthetic.main.fragment_update_blog.blog_image_item
import kotlinx.android.synthetic.main.fragment_update_blog.blog_title_item
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
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
        setupWedgiesAction()
    }

    private fun setupWedgiesAction() {
        image_container.setOnClickListener {
            if (runTimePermissionListener.isStoragePermissionGranted())
                pickImageFromGallery()
        }
    }


    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { blogPostViewState ->
                    blogPostViewState.viewBlogFields.blogPost?.let { blogPost ->
                        viewModel.onBlogPostUpdateSuccess(blogPost).let {
                            findNavController().popBackStack()
                        }
                        // on blog post updated success

                    }
                }
            }

        })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.updateBlogFields.let { updateBlogFields ->
                setBlogProperties(
                    updateBlogFields.updatedBlogTitle,
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

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == R.id.check_menu) {
            saveChanges()
            true
        } else
            super.onOptionsItemSelected(item)

    private fun saveChanges() {
        var multipart: MultipartBody.Part? = null
        viewModel.getUpdatedBlogUri()?.let { imageUri ->
            imageUri.path?.let { filePath ->
                val imageFile = File(filePath)
                Log.e(TAG, "publishNewBlogPost: imageFile: ${imageFile}")
                val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                multipart = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    requestBody
                )
            }
        }
        multipart?.let {
            viewModel.setStateEvent(
                BlogPostStateEvent.UpdatedBlogPostEvent(
                    blog_title_item.text.toString(),
                    blog_body.text.toString(),
                    it
                )
            )
            stateChangeListener.hideKeyboard()
        } ?: showErrorDialog(ErrorHandling.ERROR_MUST_SELECT_IMAGE)

    }

    override fun onPause() {
        super.onPause()
        viewModel.setUpdatedBlogFields(
            title = blog_title_item.text.toString(),
            imageUri = null,
            body = blog_body.text.toString()
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.GALLERY_REQUEST_CODE -> {
                    Log.e(TAG, "onActivityResult: GALLERY_REQUEST_CODE:")
                    data?.data?.let { uri ->
                        launchImageCrop(uri)
                    } ?: showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_IMAGE)
                }
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    Log.e(TAG, "onActivityResult: CROP_IMAGE_ACTIVITY_REQUEST_CODE:")
                    handelSuccessImageCropping(data)

                }
                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                    Log.e(TAG, "onActivityResult: CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE:")
                    showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_IMAGE)
                }
            }
        }


    }
    private fun handelSuccessImageCropping(data: Intent?) {
        val result = CropImage.getActivityResult(data)
        val uri = result.uri
        viewModel.setUpdatedBlogFields(null, uri,null)
    }

}
