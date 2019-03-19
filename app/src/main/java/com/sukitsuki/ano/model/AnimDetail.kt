package com.sukitsuki.ano.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.droidsonroids.jspoon.annotation.Selector

@Parcelize
data class AnimDetail(
  @Selector("article")
  var article: List<AnimEpisode> = emptyList(),

  @Selector("div.nav-previous > a")
  var hasNext: String = "",

  @Selector("div.nav-previous > a", attr = "href")
  var url: String = ""
) : Parcelable

@Parcelize
data class AnimEpisode(

  @Selector("h2.entry-title > a")
  var title: String = "",

  @Selector("div.entry-content > p > iframe", attr = "src")
  var url: String = ""
) : Parcelable
