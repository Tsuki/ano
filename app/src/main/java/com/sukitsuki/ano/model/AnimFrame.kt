package com.sukitsuki.ano.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.droidsonroids.jspoon.annotation.Selector

@Parcelize
data class AnimFrame(

  @Selector("video", attr = "poster")
  var poster: String = "",

  @Selector(
    "script:nth-child(3)",
    attr = "html",
    regex = "image:\"(https:\\/\\/static\\.anime1\\.me.*jpg)\""
  )
//  @Selector("title")
  var poster2: String = "",

  @Selector("source", attr = "src")
  var source: String = "",

  @Selector(
    "script:nth-child(3)",
    attr = "html",
    regex = "file:\"(https:\\/\\/\\w+.anime1\\.app\\/.*\\.mp4)"
  )
  var source2: String = ""
) : Parcelable
