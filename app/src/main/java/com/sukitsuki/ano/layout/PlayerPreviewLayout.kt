package com.sukitsuki.ano.layout

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.google.android.exoplayer2.ui.TimeBar
import com.sukitsuki.ano.R
import kotlinx.android.synthetic.main.item_timeline_preview.view.*

class PlayerPreviewLayout : FrameLayout, TimeBar.OnScrubListener {
  private var duration: Long = -1

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    LayoutInflater.from(context).inflate(R.layout.item_timeline_preview, this, true)
    visibility = View.GONE
  }


  override fun onScrubMove(timeBar: TimeBar?, position: Long) {
    if (duration < 0) return
    timeline_preview_time.text = DateUtils.formatElapsedTime(position / 1000)
  }

  override fun onScrubStart(timeBar: TimeBar?, position: Long) {
    timeline_preview_time.text = DateUtils.formatElapsedTime(position / 1000)
    visibility = View.VISIBLE
  }

  override fun onScrubStop(timeBar: TimeBar?, position: Long, canceled: Boolean) {
    visibility = View.GONE
  }
}
