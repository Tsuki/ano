package com.sukitsuki.ano.activity

import android.os.Bundle
import com.sukitsuki.ano.R
import dagger.android.AndroidInjector
import dagger.android.support.DaggerAppCompatActivity

class PreferenceActivity : DaggerAppCompatActivity() {
  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<PreferenceActivity> {

    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<PreferenceActivity>()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_preference)
  }
}
