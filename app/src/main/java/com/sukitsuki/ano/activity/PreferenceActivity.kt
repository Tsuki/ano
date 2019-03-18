package com.sukitsuki.ano.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sukitsuki.ano.R
import dagger.android.AndroidInjector

class PreferenceActivity : AppCompatActivity() {
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
