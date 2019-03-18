package com.sukitsuki.ano.repository

import io.reactivex.Observable
import retrofit2.http.GET

interface BackendRepository {

  @GET("/")
  fun index(): Observable<String>

}
