package com.example.blogapplication.ui.main

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import dagger.android.support.DaggerFragment

abstract class BaseMainFragment :DaggerFragment(){

    fun setupActionBarWithNavController(activity: AppCompatActivity, fragmentId:Int){
        val appbarConfiguration= AppBarConfiguration(setOf(fragmentId))
        NavigationUI.setupActionBarWithNavController(
            activity,
            findNavController(),
            appbarConfiguration
        )
    }

    protected fun navigateToDestination(destination: Int){
        findNavController().navigate(destination)
    }

    abstract fun cancelActiveJobs()

}