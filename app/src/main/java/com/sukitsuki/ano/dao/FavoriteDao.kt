package com.sukitsuki.ano.dao

import androidx.room.Dao
import androidx.room.Query
import com.sukitsuki.ano.entity.Favorite
import io.reactivex.Observable

@Dao
interface FavoriteDao : BaseDao<Favorite> {
  @Query("SELECT * from favorites")
  fun getAll(): Observable<List<Favorite>>
}
