package com.example.blogapplication.ui.main.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer

import com.example.blogapplication.R
import com.example.blogapplication.models.AccountProperties
import com.example.blogapplication.ui.main.account.state.AccountStateEvent
import kotlinx.android.synthetic.main.fragment_update_account.*


class UpdateAccountFragment : BaseAccountFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_account, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObserver()
        setHasOptionsMenu(true)
    }

    private fun setAccountDataFields(accountProperties: AccountProperties) {
        input_email?.let { it.setText(accountProperties.email) }
        input_username?.let { it.setText(accountProperties.username) }
    }

    private fun subscribeObserver() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                it.accountProperties?.let {
                    setAccountDataFields(it)
                }

            }

        })
    }

    private fun saveAccountChanges(){
        viewModel.setStateEvent(
            AccountStateEvent.UpdateAccountPropertiesEvent(
                username = input_username.text.toString(),
                email = input_email.text.toString()
            )
        )
        stateChangeListener.hideKeyboard()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.check_menu){
            Log.e(TAG , "onOptionsItemSelected")
            saveAccountChanges()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.updete_menu,menu)
    }

}
