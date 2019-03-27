package com.sukitsuki.ano

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sukitsuki.ano.AppConst.DatabaseName
import com.sukitsuki.ano.dao.CookieDao
import com.sukitsuki.ano.dao.FavoriteDao
import com.sukitsuki.ano.dao.WatchHistoryDao
import com.sukitsuki.ano.entity.Cookie
import com.sukitsuki.ano.entity.Favorite
import com.sukitsuki.ano.entity.WatchHistory
import com.sukitsuki.ano.utils.DateTypeConverter


@Database(
  entities = [
    Cookie::class, Favorite::class, WatchHistory::class
  ], version = 1, exportSchema = true
)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun cookieDao(): CookieDao
  abstract fun favoriteDao(): FavoriteDao
  abstract fun watchHistoryDao(): WatchHistoryDao


  companion object {
    val AppDatabase = fun(it: Application): AppDatabase {
      return Room.databaseBuilder(it.applicationContext, AppDatabase::class.java, DatabaseName)
        // .fallbackToDestructiveMigrationFrom(4, 5)
        // .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
        .fallbackToDestructiveMigrationOnDowngrade()
        // .fallbackToDestructiveMigration()
        .build()
    }
  }

}
