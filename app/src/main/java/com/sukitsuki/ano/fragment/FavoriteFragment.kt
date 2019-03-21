package com.sukitsuki.ano.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.sukitsuki.ano.R
import com.sukitsuki.ano.viewmodel.FavoriteViewModel
import dagger.android.AndroidInjector
import dagger.android.support.DaggerFragment

class FavoriteFragment : DaggerFragment() {

  companion object {
    fun newInstance() = FavoriteFragment()
  }

  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<FavoriteFragment> {
    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<FavoriteFragment>()
  }

  private lateinit var viewModel: FavoriteViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.favorite_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
    // TODO: Use the ViewModel
  }

}
