package com.sukitsuki.ano.module

import com.sukitsuki.ano.AppDatabase
import com.sukitsuki.ano.dao.CookieDao
import com.sukitsuki.ano.dao.FavoriteDao
import com.sukitsuki.ano.dao.WatchHistoryDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [])
class DaoModule {

  @Singleton
  @Provides
  fun providesCookieDao(database: AppDatabase): CookieDao = database.cookieDao()

  @Singleton
  @Provides
  fun providesFavoriteDao(database: AppDatabase): FavoriteDao = database.favoriteDao()

  @Singleton
  @Provides
  fun providesWatchHistoryDao(database: AppDatabase): WatchHistoryDao = database.watchHistoryDao()
}
