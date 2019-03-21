package com.sukitsuki.ano.module

import com.sukitsuki.ano.fragment.FavoriteFragment
import com.sukitsuki.ano.fragment.HistoryFragment
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
    , FavoriteFragment.Component::class
    , HistoryFragment.Component::class
  ]
)
abstract class FragmentModule {

  @Binds
  @IntoMap
  @ClassKey(HomeFragment::class)
  abstract fun bindHomeFragment(builder: HomeFragment.Component.Builder): AndroidInjector.Factory<*>

  @Binds
  @IntoMap
  @ClassKey(FavoriteFragment::class)
  abstract fun bindFavoriteFragment(builder: FavoriteFragment.Component.Builder): AndroidInjector.Factory<*>

  @Binds
  @IntoMap
  @ClassKey(HistoryFragment::class)
  abstract fun bindHistoryFragment(builder: HistoryFragment.Component.Builder): AndroidInjector.Factory<*>


}
