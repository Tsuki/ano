package com.sukitsuki.ano.fragment

import android.app.SearchManager
import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.HARDKEYBOARDHIDDEN_NO
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sukitsuki.ano.R
import com.sukitsuki.ano.activity.AnimDetailActivity
import com.sukitsuki.ano.adapter.AnimListAdapter
import com.sukitsuki.ano.utils.ViewModelFactory
import com.sukitsuki.ano.viewmodel.HomeViewModel
import dagger.android.AndroidInjector
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.home_fragment.*
import org.jetbrains.anko.support.v4.intentFor
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : DaggerFragment(), SearchView.OnQueryTextListener {

  override fun onQueryTextSubmit(query: String?): Boolean {
    return false
  }

  override fun onQueryTextChange(newText: String?): Boolean {
    newText?.let { animListAdapter.filter(it.trim()) } ?: run { animListAdapter.filter("") }
    return true
  }

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
        startActivity(intentFor<AnimDetailActivity>("animList" to it))
      }
    }
  }

  private var searchItem: MenuItem? = null
  private var searchManager: SearchManager? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    setHasOptionsMenu(true)
    super.onCreate(savedInstanceState)
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
    animListView.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(requireContext())
      adapter = animListAdapter
    }
    super.onViewCreated(view, savedInstanceState)
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    inflater?.inflate(R.menu.menu_anim_detail, menu)
    searchItem = menu?.findItem(R.id.action_search)
    (searchItem?.actionView as SearchView).setOnQueryTextListener(this)
    searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
      override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        Timber.i("onMenuItemActionExpand")
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        return true
      }

      override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        Timber.i("onMenuItemActionCollapse")
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayShowTitleEnabled(true)
        return true
      }

    })
    searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager?
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onConfigurationChanged(newConfig: Configuration?) {
    Timber.i("onConfigurationChanged:${newConfig?.keyboardHidden}")
    if (newConfig?.keyboardHidden == HARDKEYBOARDHIDDEN_NO) {
    }
    super.onConfigurationChanged(newConfig)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel.favorites.observe(this, Observer { homeRefreshLayout.isRefreshing = false })
  }

}
