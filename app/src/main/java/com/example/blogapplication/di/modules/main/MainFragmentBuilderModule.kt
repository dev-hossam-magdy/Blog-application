package com.example.blogapplication.di.modules.main

import com.example.blogapplication.ui.main.account.AccountFragment
import com.example.blogapplication.ui.main.account.ChangePasswordFragment
import com.example.blogapplication.ui.main.account.UpdateAccountFragment
import com.example.blogapplication.ui.main.blog.blog_fragment.BlogFragment
import com.example.blogapplication.ui.main.blog.UpdateBlogFragment
import com.example.blogapplication.ui.main.blog.ViewBlogFragment
import com.example.blogapplication.ui.main.create_blog.CreateBlogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuilderModule  {

    @ContributesAndroidInjector()
    abstract fun contributeBlogFragment(): BlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeAccountFragment(): AccountFragment

    @ContributesAndroidInjector()
    abstract fun contributeChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector()
    abstract fun contributeCreateBlogFragment(): CreateBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeUpdateBlogFragment(): UpdateBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeViewBlogFragment(): ViewBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeUpdateAccountFragment(): UpdateAccountFragment

}