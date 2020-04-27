package com.example.blogapplication.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.blogapplication.R
import com.example.blogapplication.models.AccountProperties
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.main.account.state.AccountStateEvent
import kotlinx.android.synthetic.main.fragment_account.*
import javax.inject.Inject


class AccountFragment : BaseAccountFragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupButtonsAction()
        subscribeObserver()
    }

    private fun setupButtonsAction() {
        change_password.setOnClickListener {
            navigateToDestination(R.id.action_accountFragment_to_changePasswordFragment)

        }
        logout_button.setOnClickListener {
            viewModel.setStateEvent(
                AccountStateEvent.logoutEvent()
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit) {
            navigateToDestination(R.id.action_accountFragment_to_updateAccountFragment)

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_view_menu, menu)
    }

    private fun setAccountPropertiesFields(accountProperties: AccountProperties){
        email?.setText(accountProperties.email)
        username?.setText(accountProperties.username)
    }
    private fun subscribeObserver(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            dataState?.let {
                it.data?.let {  event ->
                    event.getContentIfNotHandled()?.let {accountViewState ->
                        accountViewState.accountProperties?.let {
                            Log.e(TAG,"getting new data")
                            viewModel.setAccountPropertiesData(it)
                        }

                    }
                }

            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { accountViewState ->
            accountViewState.accountProperties?.let { accountProperties ->
                Log.e(TAG , "getting new view state ${accountProperties}")
                setAccountPropertiesFields(accountProperties)
            }

        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(
            AccountStateEvent.GetAccountPropertiesEvent()
        )
    }


}
