package com.sukitsuki.ano.repository

import com.sukitsuki.ano.model.AnimDetail
import com.sukitsuki.ano.model.AnimFrame
import com.sukitsuki.ano.model.AnimList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface BackendRepository {

  @GET("/")
  fun index(): Observable<AnimList>

  @GET("/")
  fun animDetail(@Query("cat") cat: String): Observable<AnimDetail>

  @GET
  fun animDetailNext(@Url url: String): Observable<AnimDetail>

  @GET
  fun animVideo(@Url url: String): Observable<AnimFrame>

}
