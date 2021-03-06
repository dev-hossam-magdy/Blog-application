package com.example.blogapplication.di.modules.main

import androidx.lifecycle.ViewModel
import com.codingwithmitch.openapi.di.auth.keys.ViewModelKey
import com.example.blogapplication.ui.main.account.AccountViewModel
import com.example.blogapplication.ui.main.blog.viewmodels.BlogPostViewModel
import com.example.blogapplication.ui.main.create_blog.CreateBlogViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule{

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel):ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(BlogPostViewModel::class)
    abstract fun bindBlogPostViewModel(blogPostViewModel: BlogPostViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateBlogViewModel::class)
    abstract fun bindCreateBlogViewModel(createBlogViewModel: CreateBlogViewModel):ViewModel
}