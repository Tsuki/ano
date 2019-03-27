package com.sukitsuki.ano.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.sukitsuki.ano.entity.Favorite
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface FavoriteDao : BaseDao<Favorite> {
  @Query("SELECT * from favorites order by create_at desc")
  fun getAll(): Observable<List<Favorite>>

  @Query("SELECT * from favorites where title = :title limit 1")
  fun getByTitle(title: String): Maybe<Favorite>

  @Delete
  fun deleteOne(history: Favorite)
}
