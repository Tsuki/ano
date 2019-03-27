package com.sukitsuki.ano.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.sukitsuki.ano.model.Anim
import kotlinx.android.parcel.Parcelize
import java.sql.Date

@Parcelize
@Entity(
  tableName = "favorites"
  , primaryKeys = ["title"]
//  indices = [Index(value = ["name", "domain", "path", "secure", "http_only"], unique = true)]
)
data class Favorite(
  @Embedded val anim: Anim
  , @ColumnInfo(name = "create_at") val createAt: Date = Date(java.util.Date().time)
  , @ColumnInfo(name = "update_at") var updateAt: Date = Date(java.util.Date().time)
) : Parcelable
