package com.example.blogapplication.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI

import com.example.blogapplication.R
import kotlinx.android.synthetic.main.fragment_launcher.*


class LauncherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launcher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginTextView.setOnClickListener {
            navigateToDustination(R.id.action_launcherFragment_to_loginFragment)
        }
        registerTextView.setOnClickListener {
            navigateToDustination(R.id.action_launcherFragment_to_registerFragment)
        }
        forgetPasswordTextView.setOnClickListener {
            navigateToDustination(R.id.action_launcherFragment_to_forgetPasswordFragment)
        }

    }

    private fun navigateToDustination(destinationActionId: Int) {
        findNavController().navigate(destinationActionId)
    }


}
