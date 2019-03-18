package com.sukitsuki.ano

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.sukitsuki.ano.fragment.HomeFragment
import dagger.android.AndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_app_bar_main.*

class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


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
    fragmentManager = this.supportFragmentManager
    setSupportActionBar(toolbar)
    val toggle = ActionBarDrawerToggle(
      this, drawerLayout, toolbar,
      R.string.navigation_drawer_open,
      R.string.navigation_drawer_close
    )
    toggle.syncState()
    drawerLayout.addDrawerListener(toggle)
    onNavigationItemSelected(navView.menu.findItem(R.id.nav_home))
    navView.setNavigationItemSelectedListener(this@MainActivity)

  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    val transaction by lazy { fragmentManager.beginTransaction() }
    when (item.itemId) {
      R.id.nav_home -> {
        transaction.replace(R.id.view_content_main, mHomeFragment)
        transaction.commit()
      }
      else -> {
        return false
      }
    }
    item.isChecked = true
    drawerLayout.closeDrawer(GravityCompat.START)
    return true
  }
}
