package com.sukitsuki.ano.dao

import androidx.room.Dao
import androidx.room.Query
import com.sukitsuki.ano.entity.WatchHistory
import io.reactivex.Observable

@Dao
interface WatchHistoryDao : BaseDao<WatchHistory> {

  @Query("SELECT * from watch_histories")
  fun getAll(): Observable<List<WatchHistory>>
}
