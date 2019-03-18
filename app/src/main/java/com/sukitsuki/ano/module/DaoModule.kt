package com.sukitsuki.ano.module

import com.sukitsuki.ano.AppDatabase
import com.sukitsuki.ano.dao.CookieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [])
class DaoModule {

  @Singleton
  @Provides
  fun providesAppDatabase(database: AppDatabase): CookieDao = database.cookieDao()
}
