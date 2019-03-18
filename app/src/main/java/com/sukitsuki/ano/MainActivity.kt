package com.sukitsuki.ano

import android.os.Bundle
import dagger.android.AndroidInjector
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<MainActivity> {

    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

}
