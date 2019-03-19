package com.sukitsuki.ano.module

import android.app.Application
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
import com.google.android.exoplayer2.DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import dagger.Provides
import javax.inject.Singleton

@dagger.Module(includes = [])
class ExoPlayerModule {

  @Provides
  @Singleton
  fun providesExoPlayer(app: Application): SimpleExoPlayer {
    val loadControl = DefaultLoadControl.Builder()
      .setBackBuffer(30 * 1000, false)
      .setBufferDurationsMs(
        10 * 60 * 1000,
        15 * 60 * 1000,
        DEFAULT_BUFFER_FOR_PLAYBACK_MS,
        DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
      )
    return ExoPlayerFactory.newSimpleInstance(
      app.applicationContext,
      DefaultRenderersFactory(app.applicationContext),
      DefaultTrackSelector(),
      loadControl.createDefaultLoadControl()
    )
  }

}
