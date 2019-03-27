package com.sukitsuki.ano.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.sukitsuki.ano.R
import com.sukitsuki.ano.activity.AnimDetailActivity
import com.sukitsuki.ano.adapter.HistoryAdapter
import com.sukitsuki.ano.entity.WatchHistory
import com.sukitsuki.ano.utils.SwipeToDeleteCallback
import com.sukitsuki.ano.utils.ViewModelFactory
import com.sukitsuki.ano.viewmodel.HistoryViewModel
import dagger.android.AndroidInjector
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.history_fragment.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.intentFor
import javax.inject.Inject

class HistoryFragment : DaggerFragment() {

  companion object {
    fun newInstance() = HistoryFragment()
  }

  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<HistoryFragment> {
    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<HistoryFragment>()
  }

  @Inject
  lateinit var viewModeFactory: ViewModelFactory

  private val viewModel by lazy { ViewModelProviders.of(this, viewModeFactory).get(HistoryViewModel::class.java) }

  private val listAdapter by lazy {
    HistoryAdapter(requireContext()).apply {
      this.onItemClick = { it ->
        startActivity(intentFor<AnimDetailActivity>("animList" to it.anim))
      }
      this.deleteItemFun = { doAsync { viewModel.deleteItem(it as WatchHistory) } }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    this.viewModel.history.observe(this, Observer { listAdapter.loadDataSet(it) })
    return inflater.inflate(R.layout.history_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    historyListView.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(requireContext())
      adapter = listAdapter
      ItemTouchHelper(SwipeToDeleteCallback(listAdapter)).attachToRecyclerView(this)
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel.fetchData()
  }

}
