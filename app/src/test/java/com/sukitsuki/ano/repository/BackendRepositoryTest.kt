package com.sukitsuki.ano.repository

import com.sukitsuki.ano.model.Anim
import com.sukitsuki.ano.module.RepositoryModule
import org.junit.Assert
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
      .doOnNext { println(it.list[0].getCat()) }
      .subscribe(::println)
  }

  @Test
  fun testAnim() {
    val tmp = Anim(href = "/?cat=497")
    Assert.assertEquals("497", tmp.getCat())
  }

  @Test
  fun testAnimDetail() {
    repository.animDetail("497")
      .doOnError(::println)
      .subscribe(::println)
  }

  @Test
  fun testAnimVideo() {
    repository.animVideo("https://i.animeone.me/UUKn3")
      .doOnError(::println)
      .subscribe(::println)
  }
}

