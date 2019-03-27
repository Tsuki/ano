package com.sukitsuki.ano.dao

import androidx.room.Dao
import androidx.room.Query
import com.sukitsuki.ano.entity.WatchHistory
import io.reactivex.Observable

@Dao
interface WatchHistoryDao : BaseDao<WatchHistory> {

  @Query("SELECT * from watch_histories order by create_at desc")
  fun getAll(): Observable<List<WatchHistory>>

  @Query("DELETE FROM watch_histories")
  fun deleteAll()

}
