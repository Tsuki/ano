package com.sukitsuki.ano.repository

import com.sukitsuki.ano.model.AnimList
import io.reactivex.Observable
import retrofit2.http.GET

interface BackendRepository {

  @GET("/")
  fun index(): Observable<AnimList>

}
