package com.sukitsuki.ano.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sukitsuki.ano.R
import com.sukitsuki.ano.adapter.AnimEpisodeAdapter
import com.sukitsuki.ano.model.Anim
import com.sukitsuki.ano.utils.ViewModelFactory
import com.sukitsuki.ano.viewmodel.AnimEpisodeViewModel
import dagger.android.AndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_anim_detail.*
import kotlinx.android.synthetic.main.content_anim_detail.*
import org.jetbrains.anko.longToast
import javax.inject.Inject

class AnimDetailActivity : DaggerAppCompatActivity() {

  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<AnimDetailActivity> {

    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<AnimDetailActivity>()
  }

  @Inject
  lateinit var viewModeFactory: ViewModelFactory

  private val viewModel: AnimEpisodeViewModel by lazy {
    ViewModelProviders.of(this, viewModeFactory).get(AnimEpisodeViewModel::class.java)
  }

  private val animEpisodeAdapter by lazy {
    AnimEpisodeAdapter().apply {
      this.onItemClick = { it ->
      }
    }
  }
  lateinit var animList: Anim
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    animList = intent.getParcelableExtra("animList")
    setContentView(R.layout.activity_anim_detail)
    toolbar.title = animList.title
    setSupportActionBar(toolbar)

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }

    animEpisodeList.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(context)
      adapter = animEpisodeAdapter
    }

    animList.getCat()?.let { viewModel.fetchData(it) } ?: run { longToast("Can't get cat") }
    viewModel.episode.observe(this, Observer { animEpisodeAdapter.loadDataSet(it) })
  }
}
