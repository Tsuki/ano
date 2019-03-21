package com.sukitsuki.ano.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.sukitsuki.ano.R
import com.sukitsuki.ano.viewmodel.HistoryViewModel
import dagger.android.AndroidInjector
import dagger.android.support.DaggerFragment

class HistoryFragment : DaggerFragment() {

  companion object {
    fun newInstance() = HistoryFragment()
  }

  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<HistoryFragment> {
    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<HistoryFragment>()
  }

  private lateinit var viewModel: HistoryViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.history_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
    // TODO: Use the ViewModel
  }

}
