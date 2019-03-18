package com.sukitsuki.ano.repository

import com.sukitsuki.ano.module.RepositoryModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject
import javax.inject.Singleton

@RunWith(MockitoJUnitRunner::class)
class BackendRepositoryTest {

  @Singleton
  @dagger.Component(modules = [RepositoryModule::class])
  interface TestComponent {
    fun inject(test: BackendRepositoryTest)
  }

  @Inject
  lateinit var repository: BackendRepository

  @Before
  fun setUp() {
    DaggerBackendRepositoryTest_TestComponent.create().inject(this)
  }

  @Test
  fun testIndex() {
    repository.index()
      .doOnError(::println)
      .subscribe(::println)
  }
}

