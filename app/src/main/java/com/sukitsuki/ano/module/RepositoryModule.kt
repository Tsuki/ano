package com.sukitsuki.ano.module

import com.google.gson.GsonBuilder
import com.sukitsuki.ano.AppConst
import com.sukitsuki.ano.AppDatabase
import com.sukitsuki.ano.MainApplication
import com.sukitsuki.ano.repository.CookieRepository
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
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
  fun providesOkHttpClient(cookieRepository: CookieRepository, app: MainApplication): OkHttpClient {
    val cacheSize = 10 * 1024 * 1024L // 10MB
    val builder = OkHttpClient.Builder()
      .cache(Cache(app.applicationContext.cacheDir, cacheSize))
      .cookieJar(cookieRepository)
      .addInterceptor(HttpLoggingInterceptor().apply { level = BASIC })
      .readTimeout(30, TimeUnit.SECONDS)
    return builder.build()
  }

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

//  @Singleton
//  @Provides
//  fun providesTbbRepository(retrofit: Retrofit): TbbRepository = retrofit.create(TbbRepository::class.java)

  @Singleton
  @Provides
  fun providesCookieRepository(database: AppDatabase, compositeDisposable: CompositeDisposable): CookieRepository =
    CookieRepository(database.cookieDao(), compositeDisposable)

//  @Singleton
//  @Provides
//  fun providesFavoriteRepository(database: AppDatabase): FavoriteRepository =
//    FavoriteRepository(database.favoriteDao())

  @Singleton
  @Provides
  fun providesCompositeDisposable(): CompositeDisposable = CompositeDisposable()

}
