package com.example.blogapplication.di.modules.main

import com.bumptech.glide.RequestManager
import com.example.blogapplication.adapters.BlogPostAdapter
import com.example.blogapplication.api.main.OpenAPiMainService
import com.example.blogapplication.di.anotaion.MainScope
import com.example.blogapplication.persistence.AppDatabase
import com.example.blogapplication.persistence.daos.AccountPropertiesDao
import com.example.blogapplication.persistence.daos.BlogPostDao
import com.example.blogapplication.repository.main.AccountRepository
import com.example.blogapplication.repository.main.BlogPostRepository
import com.example.blogapplication.repository.main.CreateBlogRepository
import com.example.blogapplication.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideApiMainService(retrofitBuilder: Retrofit.Builder): OpenAPiMainService =
        retrofitBuilder
            .build()
            .create(OpenAPiMainService::class.java)

    @MainScope
    @Provides
    fun provideAccountRepo(
        openAPiMainService: OpenAPiMainService,
        sessionManager: SessionManager,
        accountPropertiesDao: AccountPropertiesDao
    ): AccountRepository =
        AccountRepository(openAPiMainService, accountPropertiesDao, sessionManager)

    @MainScope
    @Provides
    fun provideBlogPostDao(db: AppDatabase): BlogPostDao =
        db.getBlogPostDao()

    @MainScope
    @Provides
    fun provideBlogPostRepository(
        sessionManager: SessionManager,
        blogPostDao: BlogPostDao,
        openAPiMainService: OpenAPiMainService
    ): BlogPostRepository =
        BlogPostRepository(sessionManager, blogPostDao, openAPiMainService)

    @MainScope
    @Provides
    fun provideBlogPostAdapter(requestManager: RequestManager): BlogPostAdapter =
        BlogPostAdapter(requestManager)

    @MainScope
    @Provides
    fun provideCreateBlogRepository(
        sessionManager: SessionManager,
        openAPiMainService: OpenAPiMainService,
        blogPostDao: BlogPostDao
    ): CreateBlogRepository =
        CreateBlogRepository(
            sessionManager = sessionManager,
            blogPostDao = blogPostDao,
            openAPiMainService = openAPiMainService
        )

}