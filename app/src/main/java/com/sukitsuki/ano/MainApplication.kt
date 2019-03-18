package com.sukitsuki.ano

import com.sukitsuki.ano.module.AppModule
import com.sukitsuki.ano.module.RepositoryModule
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Singleton

class MainApplication : DaggerApplication() {

  @Singleton
  @dagger.Component(
    modules = [
      AndroidInjectionModule::class
      , AppModule::class
      , RepositoryModule::class
    ]
  )
  interface Component : AndroidInjector<MainApplication> {
    @dagger.Component.Builder
    abstract class Builder : AndroidInjector.Builder<MainApplication>() {
      abstract fun plus(appModule: AppModule): Builder
    }
  }


  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerMainApplication_Component.builder().plus(AppModule(this)).create(this)
  }

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
  }
}
