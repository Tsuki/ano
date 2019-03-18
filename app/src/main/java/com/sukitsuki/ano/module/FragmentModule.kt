package com.sukitsuki.ano.module

import com.sukitsuki.ano.fragment.HomeFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(
  includes = [ViewModelFactoryModule::class],
  subcomponents = [
    HomeFragment.Component::class
  ]
)
abstract class FragmentModule {

  @Binds
  @IntoMap
  @ClassKey(HomeFragment::class)
  abstract fun bindHomeFragment(builder: HomeFragment.Component.Builder): AndroidInjector.Factory<*>


}
