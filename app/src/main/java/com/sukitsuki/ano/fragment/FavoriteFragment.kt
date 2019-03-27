package com.sukitsuki.ano.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sukitsuki.ano.R
import com.sukitsuki.ano.activity.AnimDetailActivity
import com.sukitsuki.ano.adapter.FavoriteAdapter
import com.sukitsuki.ano.utils.ViewModelFactory
import com.sukitsuki.ano.viewmodel.FavoriteViewModel
import dagger.android.AndroidInjector
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.favorite_fragment.*
import org.jetbrains.anko.support.v4.intentFor
import javax.inject.Inject

class FavoriteFragment : DaggerFragment() {

  companion object {
    fun newInstance() = FavoriteFragment()
  }

  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<FavoriteFragment> {
    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<FavoriteFragment>()
  }

  @Inject
  lateinit var viewModeFactory: ViewModelFactory

  private val viewModel by lazy { ViewModelProviders.of(this, viewModeFactory).get(FavoriteViewModel::class.java) }

  private val favoriteListAdapter by lazy {
    FavoriteAdapter(requireContext()).apply {
      this.onItemClick = { it ->
        startActivity(intentFor<AnimDetailActivity>("animList" to it.anim))
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    this.viewModel.favorites.observe(this, Observer { favoriteListAdapter.loadDataSet(it) })
    return inflater.inflate(R.layout.favorite_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    favoriteListView.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(requireContext())
      adapter = favoriteListAdapter
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel.fetchData()

  }
}
