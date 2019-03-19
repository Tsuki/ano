package com.sukitsuki.ano.activity

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.sukitsuki.ano.R
import dagger.android.AndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_anim_detail.*

class AnimDetailActivity : DaggerAppCompatActivity() {

  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<AnimDetailActivity> {

    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<AnimDetailActivity>()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_anim_detail)
    setSupportActionBar(toolbar)
    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }
  }
}
