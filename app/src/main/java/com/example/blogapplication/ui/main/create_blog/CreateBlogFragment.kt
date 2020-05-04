package com.example.blogapplication.ui.main.create_blog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import com.example.blogapplication.R
import com.example.blogapplication.ui.*
import com.example.blogapplication.ui.main.create_blog.state.CreateBlogStateEvent
import com.example.blogapplication.util.Constants
import com.example.blogapplication.util.ErrorHandling
import com.example.blogapplication.util.SuccessHandling
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_create_blog.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.Multipart
import java.io.File

class CreateBlogFragment : BaseCreateBlogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupWedgiesAction()
        subscribeObservers()
    }

    private fun setupWedgiesAction() {
        blog_image_item.setOnClickListener {
            if (runTimePermissionListener.isStoragePermissionGranted())
                pickImageFromGallery()
        }
        update_textview.setOnClickListener {
            if (runTimePermissionListener.isStoragePermissionGranted())
                pickImageFromGallery()
        }
    }




    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            dataState.response?.let { event ->
                event.peekContent().message?.let { message->
                    if (message.equals(SuccessHandling.SUCCESS_BLOG_CREATED))
                        viewModel.clearNewBlogFields()
                }

            }
        })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.newBlogFields.let { newBlogFields ->
                setBlogProperties(
                    newBlogFields.newBlogTitle,
                    newBlogFields.newBlogBody,
                    newBlogFields.newBlogImageUri
                )

            }
        })
    }

    private fun setBlogProperties(title: String?, body: String?, imageUri: Uri?) {
        imageUri?.let { requestManager.load(it).into(blog_image_item) } ?: setDefaultImage()
        title?.let { blog_title_item.setText(it) }
        body?.let { blog_body.setText(it) }
    }

    private fun setDefaultImage() {
        requestManager
            .load(R.drawable.default_image)
            .into(blog_image_item)
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

    private fun publishNewBlogPost() {
        var multipart: MultipartBody.Part? = null
        viewModel.getImageUri()?.let { imageUri ->
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
                CreateBlogStateEvent.CreateNewBlogStateEvent(
                    blog_title_item.text.toString(),
                    blog_body.text.toString(),
                    it
                )
            )
            stateChangeListener.hideKeyboard()
        } ?: showErrorDialog(ErrorHandling.ERROR_MUST_SELECT_IMAGE)

    }

    private fun handelSuccessImageCropping(data: Intent?) {
        val result = CropImage.getActivityResult(data)
        val uri = result.uri
        viewModel.setNewBlogFields(null, null, uri)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.publish_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (item.itemId == R.id.publish) {
           val callback :AreYouSureCallback = object :AreYouSureCallback{
               override fun proceed() {
                   publishNewBlogPost()
               }

               override fun cancel() {

               }

           }
            uiCommunicationListener.onUIMessageReceived(UIMessage(
                getString(R.string.are_you_sure_publish),
                UIMessageType.AreYouSureDialog(callback)
            ))
            true
        } else super.onOptionsItemSelected(item)


    override fun onPause() {
        super.onPause()
        viewModel.setNewBlogFields(
            blog_title_item.text.toString(),
            blog_body.text.toString(),
            null
        )
    }
}
