package com.sukitsuki.ano.module

import android.app.Application
import com.google.gson.GsonBuilder
import com.sukitsuki.ano.AppConst
import com.sukitsuki.ano.AppConst.cacheSize
import com.sukitsuki.ano.dao.CookieDao
import com.sukitsuki.ano.repository.BackendRepository
import com.sukitsuki.ano.repository.CookieRepository
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module(includes = [])
class RepositoryModule {

  @Singleton
  @Provides
//  fun providesOkHttpClient(cookieRepository: CookieRepository, app: MainApplication): OkHttpClient {
  fun providesOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()
//      .cache(Cache(app.applicationContext.cacheDir, cacheSize))
//      .cookieJar(cookieRepository)
      .addInterceptor(HttpLoggingInterceptor().apply { level = BASIC })
      .readTimeout(30, TimeUnit.SECONDS)
    return builder.build()
  }

  @Singleton
  @Provides
  fun providesOkHttpCache(app: Application): Cache = Cache(app.applicationContext.cacheDir, cacheSize)

  @Singleton
  @Provides
  fun providesRetrofit(httpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      // Show primitive/String Type
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(JspoonConverterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .baseUrl(AppConst.BASE_URL)
      .client(httpClient)
      .build()
  }

  @Singleton
  @Provides
  fun providesBackendRepository(retrofit: Retrofit): BackendRepository = retrofit.create(BackendRepository::class.java)

  @Singleton
  @Provides
  fun providesCookieRepository(cookieDao: CookieDao): CookieRepository = CookieRepository(cookieDao)

}
