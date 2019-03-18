package com.sukitsuki.ano.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sukitsuki.ano.R
import com.sukitsuki.ano.utils.ViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

  companion object {
    fun newInstance() = HomeFragment()
  }

  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<HomeFragment> {
    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<HomeFragment>()
  }

  @Inject
  lateinit var viewModeFactory: ViewModelFactory

  private val viewModel: HomeViewModel by lazy {
    ViewModelProviders.of(this, viewModeFactory).get(HomeViewModel::class.java)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.home_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel.favorites.observe(this, Observer {
      if (it.isNotEmpty()) Timber.i("${it[0]}")
    })
  }

}
