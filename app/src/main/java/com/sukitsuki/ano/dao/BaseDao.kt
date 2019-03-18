package com.sukitsuki.ano.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(vararg obj: T): List<Long>

  @Update
  fun update(vararg obj: T)

  @Delete
  fun delete(obj: T)
}
