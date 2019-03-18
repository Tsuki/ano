package com.sukitsuki.ano.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
data class WatchHistory(
  @PrimaryKey val id: Int
) : Parcelable
