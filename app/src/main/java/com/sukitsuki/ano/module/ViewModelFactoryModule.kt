package com.sukitsuki.ano.module

import androidx.lifecycle.ViewModelProvider
import com.sukitsuki.ano.utils.ViewModelFactory
import dagger.Binds
import javax.inject.Singleton

@dagger.Module(includes = [ViewModelModule::class])
abstract class ViewModelFactoryModule {

  @Binds
  @Singleton
  abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}
