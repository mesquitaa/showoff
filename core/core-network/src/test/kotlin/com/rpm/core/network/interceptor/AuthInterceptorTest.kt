package com.rpm.core.network.interceptor

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import kotlin.test.assertEquals

class AuthInterceptorTest {
  companion object {
    const val TOKEN = "mock_token"
  }

  private val subject by lazy { AuthInterceptor(TOKEN) }

  private val mockWebServer by lazy { MockWebServer() }

  @BeforeEach
  fun setUp() = mockWebServer.start()

  @AfterEach
  fun tearDown() = mockWebServer.shutdown()

  @Test
  @Throws(IOException::class)
  fun `intercept should add api_key query parameter`() {
    // GIVEN
    mockWebServer.enqueue(MockResponse().setBody("{}"))
    val okHttpClient = OkHttpClient.Builder().addInterceptor(subject).build()
    val request = Request.Builder().url(mockWebServer.url("/test")).build()

    // WHEN
    val response = okHttpClient.newCall(request).execute()

    // THEN
    assertTrue(response.isSuccessful)
    val recordedRequest = mockWebServer.takeRequest()
    assertEquals("Bearer $TOKEN", recordedRequest.headers["Authorization"])
  }
}
