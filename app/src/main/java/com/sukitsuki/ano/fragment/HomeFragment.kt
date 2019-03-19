package com.sukitsuki.ano.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sukitsuki.ano.R
import com.sukitsuki.ano.adapter.AnimListAdapter
import com.sukitsuki.ano.utils.ViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.home_fragment.*
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

  private val animListAdapter by lazy {
    AnimListAdapter(requireContext()).apply {
      this.onItemClick = { it ->
        //        val intent = Intent(context, AnimeDetailActivity::class.java)
//        intent.putExtra("animeList", it)
//        startActivity(intent)
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    this.viewModel.favorites.observe(this, Observer { animListAdapter.loadDataSet(it) })
    return inflater.inflate(R.layout.home_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    homeRefreshLayout.setOnRefreshListener { viewModel.fetchData() }
    animeListView.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(requireContext())
      adapter = animListAdapter
    }
    super.onViewCreated(view, savedInstanceState)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel.favorites.observe(this, Observer { homeRefreshLayout.isRefreshing = false })
  }

}
