package com.sukitsuki.ano.activity

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
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
import org.jetbrains.anko.sdk27.coroutines.onSystemUiVisibilityChange
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

  private val mHideHandler = Handler()
  private val mHideRunnable = Runnable { mHideHandler.postDelayed(mHidePart2Runnable, 300L) }
  private val mHidePart2Runnable = Runnable {
    activity_anim_detail.systemUiVisibility = (
      View.SYSTEM_UI_FLAG_LOW_PROFILE
        or View.SYSTEM_UI_FLAG_FULLSCREEN
//        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
      )
  }

  private val viewModel: AnimEpisodeViewModel by lazy {
    ViewModelProviders.of(this, viewModeFactory).get(AnimEpisodeViewModel::class.java)
  }

  private val animEpisodeAdapter by lazy {
    AnimEpisodeAdapter(backendRepository).apply {
      this.onItemClick = { it, it2 ->
        when {
          it.source != "" -> replace(it.source)
          it.poster2 != "" -> replaceMp4(it.source2)
          else -> longToast("Cannot load source")
        }
        toolbar?.let { tb -> tb.title = it2.title }
      }
    }
  }
  lateinit var animList: Anim
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    animList = intent.getParcelableExtra("animList")
    setContentView(R.layout.activity_anim_detail)
    ButterKnife.bind(this)

    window.decorView.onSystemUiVisibilityChange {
      if ((it and View.SYSTEM_UI_FLAG_FULLSCREEN) == 0
        && requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
      ) delayedHide()
    }

    toolbar?.let {
      it.title = animList.title
      setSupportActionBar(it)
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    playerView.player = simpleExoPlayer

    animEpisodeList?.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(context)
      adapter = animEpisodeAdapter
    }

    animList.getCat()?.let { viewModel.fetchData(it) } ?: run { longToast("Can't get cat") }
    viewModel.episode.observe(this, Observer { animEpisodeAdapter.loadDataSet(it) })
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    when (requestedOrientation) {
      ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE -> {
        Timber.i("SCREEN_ORIENTATION_LANDSCAPE")
        delayedHide()
      }
      ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> {
        Timber.i("SCREEN_ORIENTATION_PORTRAIT")
      }
      else -> {
        Timber.i("requestedOrientation:$requestedOrientation")
      }
    }
  }

  private fun delayedHide(uiAnimationDelay: Long = 1000L) {
    mHideHandler.removeCallbacks(mHideRunnable)
    mHideHandler.postDelayed(mHideRunnable, uiAnimationDelay)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      // Use android.R.id rather than packaged id
      android.R.id.home -> {
        onBackPressed()
        return true
      }
      else -> {
        Timber.d("itemId${item?.itemId?.let { resources.getResourceName(it).split("\\/") }}")
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun finish() {
    Timber.i("finished")
    simpleExoPlayer.stop(true)
    super.finish()
  }

  @OnClick(R.id.exo_fullscreen_icon)
  fun fullscreen() {
    when (requestedOrientation) {
      ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE -> {
        Timber.i("SCREEN_ORIENTATION_LANDSCAPE")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mHidePart2Runnable.run()
      }
      ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> {
        Timber.i("SCREEN_ORIENTATION_PORTRAIT")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
      }
      else -> {
        Timber.i("requestedOrientation:$requestedOrientation")
      }
    }
  }

  override fun onBackPressed() {
    if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
      fullscreen()
    else
      super.onBackPressed()
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

  private fun replaceMp4(url: String) {
    val httpDataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(applicationContext, "ano"), null)
    httpDataSourceFactory.defaultRequestProperties.set("Referer", "https://anime1.me/")
    val extractorsFactory = DefaultExtractorsFactory()
    val mediaSource = ExtractorMediaSource.Factory(httpDataSourceFactory)
      .setExtractorsFactory(extractorsFactory).createMediaSource(url.toUri())
    simpleExoPlayer.playWhenReady = true
    simpleExoPlayer.prepare(mediaSource)

    Timber.d("replace: $url")
  }

}
