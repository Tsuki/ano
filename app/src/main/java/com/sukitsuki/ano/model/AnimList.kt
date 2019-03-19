package com.sukitsuki.ano.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.droidsonroids.jspoon.annotation.Selector

@Parcelize
data class AnimList(
  @Selector("tbody > tr")
  var list: List<Anim> = emptyList()
) : Parcelable

@Parcelize
data class Anim(

  @Selector("td.column-1 > a", attr = "href")
  var href: String = "",

  @Selector("td.column-1")
  var title: String = "",

  @Selector("td.column-2")
  var episodeTitle: String = "",

  @Selector("td.column-3")
  var year: String = "",

  @Selector("td.column-4")
  var season: String = "",

  @Selector("td.column-5")
  var fanSubGroup: String = ""
) : Parcelable {
  fun getCat(): String? {
    val regex = "/\\?cat=(\\d+)".toRegex()
    val matchResult = regex.find(this.href)
    return matchResult?.let {
      val (cat: String) = matchResult.destructured
      return cat
    }
  }
}
