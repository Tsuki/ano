package com.sukitsuki.ano

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.FragmentManager
import com.sukitsuki.ano.fragment.HomeFragment
import dagger.android.AndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_app_bar_main.*

class MainActivity : DaggerAppCompatActivity() {

  private lateinit var fragmentManager: FragmentManager
  private val mHomeFragment by lazy { HomeFragment.newInstance() }

  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<MainActivity> {

    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)
    val toggle = ActionBarDrawerToggle(
      this, drawerLayout, toolbar,
      R.string.navigation_drawer_open,
      R.string.navigation_drawer_close
    )
    toggle.syncState()
    drawerLayout.addDrawerListener(toggle)

  }

}
