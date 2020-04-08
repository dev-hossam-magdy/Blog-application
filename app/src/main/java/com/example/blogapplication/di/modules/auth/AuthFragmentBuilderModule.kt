package com.example.blogapplication.di.modules.auth

import com.example.blogapplication.ui.auth.ForgetPasswordFragment
import com.example.blogapplication.ui.auth.LauncherFragment
import com.example.blogapplication.ui.auth.LoginFragment
import com.example.blogapplication.ui.auth.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBuilderModule {

    @ContributesAndroidInjector()
    abstract fun contributesLauncherFragment():LauncherFragment

    @ContributesAndroidInjector()
    abstract fun contributeLoginFragment():LoginFragment

    @ContributesAndroidInjector()
    abstract fun contributeForgetpasswordFragment():ForgetPasswordFragment

    @ContributesAndroidInjector()
    abstract fun contributeRegisterFragment():RegisterFragment


}