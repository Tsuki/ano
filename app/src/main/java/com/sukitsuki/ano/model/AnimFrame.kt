package com.sukitsuki.ano.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.droidsonroids.jspoon.annotation.Selector

@Parcelize
data class AnimFrame(

  @Selector("video", attr = "poster")
  var poster: String = "",

  @Selector("source", attr = "src")
  var source: String = ""
) : Parcelable
