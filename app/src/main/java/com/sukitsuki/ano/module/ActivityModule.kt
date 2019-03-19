package com.sukitsuki.ano.module

import com.sukitsuki.ano.MainActivity
import com.sukitsuki.ano.activity.AnimDetailActivity
import com.sukitsuki.ano.activity.PreferenceActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(
  includes = [ViewModelFactoryModule::class],
  subcomponents = [
    MainActivity.Component::class
    , PreferenceActivity.Component::class
    , AnimDetailActivity.Component::class
  ]
)
abstract class ActivityModule {

  @Binds
  @IntoMap
  @ClassKey(MainActivity::class)
  abstract fun bindMainActivity(builder: MainActivity.Component.Builder): AndroidInjector.Factory<*>

  @Binds
  @IntoMap
  @ClassKey(PreferenceActivity::class)
  abstract fun bindPreferenceActivity(builder: PreferenceActivity.Component.Builder): AndroidInjector.Factory<*>

  @Binds
  @IntoMap
  @ClassKey(AnimDetailActivity::class)
  abstract fun bindAnimDetailActivity(builder: AnimDetailActivity.Component.Builder): AndroidInjector.Factory<*>

}
