package com.sukitsuki.ano.activity

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.sukitsuki.ano.R
import com.sukitsuki.ano.adapter.AnimEpisodeAdapter
import com.sukitsuki.ano.model.Anim
import com.sukitsuki.ano.repository.BackendRepository
import com.sukitsuki.ano.utils.ViewModelFactory
import com.sukitsuki.ano.viewmodel.AnimEpisodeViewModel
import dagger.android.AndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_anim_detail.*
import org.jetbrains.anko.longToast
import timber.log.Timber
import javax.inject.Inject

class AnimDetailActivity : DaggerAppCompatActivity() {

  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<AnimDetailActivity> {

    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<AnimDetailActivity>()
  }

  @Inject
  lateinit var viewModeFactory: ViewModelFactory
  @Inject
  lateinit var backendRepository: BackendRepository
  @Inject
  lateinit var simpleExoPlayer: SimpleExoPlayer

  private val viewModel: AnimEpisodeViewModel by lazy {
    ViewModelProviders.of(this, viewModeFactory).get(AnimEpisodeViewModel::class.java)
  }

  private val animEpisodeAdapter by lazy {
    AnimEpisodeAdapter(backendRepository).apply {
      this.onItemClick = { it ->
        Timber.i("$it")
        if (it.source != "") replace(it.source)
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

//    fab.setOnClickListener { view ->
//      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//        .setAction("Action", null).show()
//    }
    playerView.player = simpleExoPlayer

    animEpisodeList.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(context)
      adapter = animEpisodeAdapter
    }

    animList.getCat()?.let { viewModel.fetchData(it) } ?: run { longToast("Can't get cat") }
    viewModel.episode.observe(this, Observer { animEpisodeAdapter.loadDataSet(it) })
  }

  private fun replace(url: String) {
    val httpDataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(applicationContext, "ano"), null)
    httpDataSourceFactory.defaultRequestProperties.set("Referer", "https://anime1.me/")
    val defaultHlsExtractorFactory =
      DefaultHlsExtractorFactory(DefaultTsPayloadReaderFactory.FLAG_DETECT_ACCESS_UNITS or DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES)
    val hlsMediaSource = HlsMediaSource.Factory(httpDataSourceFactory)
      .setExtractorFactory(defaultHlsExtractorFactory)
      .setAllowChunklessPreparation(true)
      .createMediaSource(Uri.parse(url))
    simpleExoPlayer.playWhenReady = true
    simpleExoPlayer.prepare(hlsMediaSource)

    Timber.d("replace: $url")
  }
}
