package com.example.blogapplication.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.blogapplication.ui.*
import com.example.blogapplication.util.Constants
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.android.support.DaggerFragment

abstract class BaseMainFragment :DaggerFragment(){
    protected lateinit var stateChangeListener: DataStateChangesListener
    protected lateinit var runTimePermissionListener: RunTimePermissionListener

    fun setupActionBarWithNavController(activity: AppCompatActivity, fragmentId:Int){
        val appbarConfiguration= AppBarConfiguration(setOf(fragmentId))
        NavigationUI.setupActionBarWithNavController(
            activity,
            findNavController(),
            appbarConfiguration
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            stateChangeListener = context as DataStateChangesListener
        }catch(e: ClassCastException){
            Log.e("BaseMainFragment", "$context must implement DataStateChangesListener" )
        }

        try {
            runTimePermissionListener = context as RunTimePermissionListener
        }catch (e:ClassCastException){
            Log.e("BaseMainFragment", "$context must implement runTimePermissionListener" )
        }
    }

    protected open fun navigateToDestination(destination: Int){
        findNavController().navigate(destination)
    }

    abstract fun cancelActiveJobs()
    protected fun showErrorDialog(errorMessage: String) {
        stateChangeListener.onDataStateChange(
            DataState.None<Any>(
                response = Response(
                    message = errorMessage,
                    responseType = ResponseType.Dialog()
                )
            )
        )
    }

    protected fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpg", "image/png", "image/jpeg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE)
    }

    protected fun launchImageCrop(uri: Uri?) {
        context?.let {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(it, this);
        }
    }
}