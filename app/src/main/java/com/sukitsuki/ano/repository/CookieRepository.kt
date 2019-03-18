package com.sukitsuki.ano.repository

import com.sukitsuki.ano.dao.CookieDao
import com.sukitsuki.ano.entity.Cookie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.CookieJar
import okhttp3.HttpUrl
import timber.log.Timber


class CookieRepository(private val cookieDao: CookieDao) : CookieJar {

  private var cookies: Map<String, List<okhttp3.Cookie>> =
    cookieDao.getAll().blockingFirst().map(Cookie::toOkHttpCookie).groupBy(okhttp3.Cookie::domain)

  init {
    subscribeFromSQLite()
  }

  private fun subscribeFromSQLite(): Disposable? {
    return cookieDao.getAll()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError { Timber.w(it) }
      .doOnNext { Timber.d("subscribeFromSQLite") }
      .map { it.map(Cookie::toOkHttpCookie) }
      .map { it.groupBy(okhttp3.Cookie::domain) }
      .onErrorReturn { emptyMap() }
      .subscribe { cookies = it }
  }

  private fun saveCookieToSQLite(cookies: List<okhttp3.Cookie>) {
    Timber.d("saveCookieToSQLite ${cookies.map { Cookie(it) }.joinToString("\n")}")
    cookieDao.insert(*(cookies.map { Cookie(it) }.toTypedArray()))
  }

  override fun saveFromResponse(url: HttpUrl, cookies: List<okhttp3.Cookie>) {
    saveCookieToSQLite(cookies)
  }

  override fun loadForRequest(url: HttpUrl): List<okhttp3.Cookie> {
    return cookies[url.host()] ?: emptyList()
  }

}
