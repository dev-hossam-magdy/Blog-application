package com.example.blogapplication.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.example.blogapplication.R
import com.example.blogapplication.base.BaseActivity
import com.example.blogapplication.ui.DataStateChangesListener
import com.example.blogapplication.ui.ToolBarExpandingListener
import com.example.blogapplication.ui.auth.AuthActivity
import com.example.blogapplication.ui.main.account.ChangePasswordFragment
import com.example.blogapplication.ui.main.account.UpdateAccountFragment
import com.example.blogapplication.ui.main.blog.UpdateBlogFragment
import com.example.blogapplication.ui.main.blog.ViewBlogFragment
import com.example.blogapplication.util.BottomNavController
import com.example.blogapplication.util.setUpNavigation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(),
    BottomNavController.NavGraphProvider,
    BottomNavController.OnNavigationGraphChanged,
    BottomNavController.OnNavigationReselectedListener,
    ToolBarExpandingListener {
    override val TAG: String
        get() = "MainActivity"
    override val progressBar: ProgressBar
        get() = mainProgressBar
    private lateinit var bottomNavigationView: BottomNavigationView
    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.menu_item_blog,
            this,
            this
        )
    }

    override fun getNavGraphId(itemId: Int): Int = when (itemId) {
        R.id.menu_item_account -> R.navigation.nav_account
        R.id.menu_item_blog -> R.navigation.nav_blog
        R.id.menu_item_create_blog -> R.navigation.nav_create_blog
        else -> R.navigation.nav_blog
    }

    override fun onGraphChange() {
        expandAppbar()
        cancelActiveJobs()

    }

    private fun cancelActiveJobs() {
        val fragments = bottomNavController
            .fragmentManager
            .findFragmentById(bottomNavController.containerId)
            ?.childFragmentManager
            ?.fragments
        fragments?.let {
            for (fragment in fragments)
                if(fragment is BaseMainFragment){
                    fragment.cancelActiveJobs()
                }
        }
    }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment)  =
        when(fragment){
            is ViewBlogFragment -> navController.navigate(R.id.action_viewBlogFragment_to_blogFragment)
            is UpdateBlogFragment -> navController.navigate(R.id.action_updateBlogFragment_to_blogFragment)
            is UpdateAccountFragment -> navController.navigate(R.id.action_updateAccountFragment_to_accountFragment)
            is ChangePasswordFragment -> navController.navigate(R.id.action_changePasswordFragment_to_accountFragment)
            else ->{}
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar()
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpNavigation(bottomNavController,this)
        if (savedInstanceState == null)
            bottomNavController.onNavigationItemSelected()


        subscribeObservers()
    }


    override fun subscribeObservers() {
        sessionManager.cachedToken.observe(this, Observer { authToken ->
            Log.d(TAG, "MainActivity, subscribeObservers: ViewState: ${authToken}")
            if (authToken == null || authToken.accountPk == -1 || authToken.token == null) {
                navAuthActivity()
                finish()
            }
        })
    }

    private fun setupActionBar() {
        setSupportActionBar(tool_bar)
    }

    private fun navAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun expandAppbar() {
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }
}
