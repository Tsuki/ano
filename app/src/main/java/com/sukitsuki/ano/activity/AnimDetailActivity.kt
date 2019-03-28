package com.sukitsuki.ano.activity

import android.Manifest
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.TextureView
import android.view.View
import android.view.WindowManager
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.BindColor
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.google.android.exoplayer2.Player
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
import com.sukitsuki.ano.dao.FavoriteDao
import com.sukitsuki.ano.dao.WatchHistoryDao
import com.sukitsuki.ano.entity.Favorite
import com.sukitsuki.ano.entity.WatchHistory
import com.sukitsuki.ano.model.Anim
import com.sukitsuki.ano.model.AnimFrame
import com.sukitsuki.ano.utils.ViewModelFactory
import com.sukitsuki.ano.utils.showRationale
import com.sukitsuki.ano.utils.takeScreenshot
import com.sukitsuki.ano.viewmodel.AnimEpisodeViewModel
import dagger.android.AndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_anim_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.sdk27.coroutines.onSystemUiVisibilityChange
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import permissions.dispatcher.*
import timber.log.Timber
import javax.inject.Inject

@RuntimePermissions
class AnimDetailActivity : DaggerAppCompatActivity(), Player.EventListener {

  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<AnimDetailActivity> {

    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<AnimDetailActivity>()
  }

  @Inject
  lateinit var viewModeFactory: ViewModelFactory
  @Inject
  lateinit var simpleExoPlayer: SimpleExoPlayer
  @Inject
  lateinit var mFavoriteDao: FavoriteDao
  @Inject
  lateinit var mWatchHistoryDao: WatchHistoryDao

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
  private var mFavorite: Favorite? = null
  private val mCompositeDisposable = CompositeDisposable()

  private val animEpisodeAdapter by lazy { AnimEpisodeAdapter(viewModel) }
  private lateinit var animList: Anim

  @BindColor(R.color.colorAccent)
  @JvmField
  var bookmarkActive: Int = 0

  @BindColor(R.color.primaryTextColor)
  @JvmField
  var bookmarkInactive: Int = 0

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
    simpleExoPlayer.addListener(this@AnimDetailActivity)
    onPlayerStateChanged(simpleExoPlayer.playWhenReady, simpleExoPlayer.playbackState)

    toolbar?.let {
      it.title = animList.title
      setSupportActionBar(it)
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
      return@let
    }

    playerView.player = simpleExoPlayer

    animEpisodeList?.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(context)
      adapter = animEpisodeAdapter
    }

    bookmarkFab?.let { updateBookmark() }

    animList.getCat()?.let { cat -> viewModel.fetchData(cat) }
    viewModel.episode.observe(this, Observer { animEpisodeAdapter.loadDataSet(it) })
    viewModel.animEpisode.observe(this, Observer {
      toolbar?.let { tb -> tb.subtitle = it.title }
    })
    viewModel.animFrame.observe(this, Observer {
      if (it == AnimFrame()) return@Observer
      when {
        it.source != "" -> replace(it.source)
        it.poster2 != "" -> replaceMp4(it.source2)
      }
      viewModel.animFrame.value = AnimFrame()
    })
    doAsync { mWatchHistoryDao.insert(WatchHistory(animList)) }
  }


  private fun updateBookmark() {
    mFavoriteDao.getByTitle(animList.title)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError { Timber.w(it) }
      .onErrorReturn { null }
      .subscribe {
        mFavorite = it
        updateBookmarkColor()
      }.addTo(mCompositeDisposable)
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

  override fun onDestroy() {
    super.onDestroy()
    mCompositeDisposable.dispose()
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

  @OnClick(R.id.bookmarkFab)
  @Optional
  fun bookmark() {
    doAsync {
      mFavorite?.let {
        mFavoriteDao.delete(it)
        mFavorite = null
      } ?: run {
        mFavorite = Favorite(animList)
        mFavoriteDao.insert(mFavorite!!)
        return@run
      }
      uiThread {
        updateBookmarkColor()
      }
    }
  }

  private fun updateBookmarkColor() {
    mFavorite?.let { bookmarkFab?.setColorFilter(bookmarkActive) }
      ?: run { bookmarkFab?.setColorFilter(bookmarkInactive) }
  }

  override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
    when {
      playbackState == Player.STATE_BUFFERING || playbackState == Player.STATE_READY && playWhenReady ->
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
      playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED || playbackState == Player.STATE_READY && !playWhenReady ->
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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

  @OnClick(R.id.exo_screenshot)
  internal fun screenshotPermissionCheck() {
    if (simpleExoPlayer.playbackState != Player.STATE_READY) return run { longToast("非播放中") }
    if (playerView.videoSurfaceView !is TextureView) {
      longToast("Only support in TextureView")
      return
    }
    screenshotWithPermissionCheck()
  }

  @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
  internal fun screenshot() {
    toast("己保存在 ${takeScreenshot((playerView.videoSurfaceView as TextureView).bitmap)}")
  }

  @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
  fun showRationaleForStorage(request: PermissionRequest) =
    showRationale(request, "截圖需要保存權限，應用將要申請保存權限")


  @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
  fun showDenied() {
    longToast("沒有權限保存截圖")
  }

  @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
  fun onNeverAskAgain() {
    longToast("您已經禁止了保存權限,是否現在去開啓")
  }

}
