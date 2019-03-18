package com.sukitsuki.ano.dao

import androidx.room.Dao
import androidx.room.Query
import com.sukitsuki.ano.entity.Cookie
import io.reactivex.Observable

@Dao
interface CookieDao : BaseDao<Cookie> {

  @Query("SELECT * from cookies")
  fun getAll(): Observable<List<Cookie>>

  @Query("SELECT * from cookies where name = :name and domain = :domain")
  fun findByNameDomain(name: String, domain: String): List<Cookie>

  @Query("SELECT * from cookies where domain = :domain")
  fun findByDomain(domain: String): List<Cookie>

}
