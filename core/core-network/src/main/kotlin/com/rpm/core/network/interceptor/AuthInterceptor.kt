package com.rpm.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
  private val token: String,
) : Interceptor {
  companion object {
    private const val BEARER_NAME_KEY = "Authorization"
    private const val BEARER_TOKEN = "Bearer %s"
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()

    val newRequest = originalRequest
      .newBuilder()
      .header(
        name = BEARER_NAME_KEY,
        value = BEARER_TOKEN.format(token),
      ).build()

    return chain.proceed(newRequest)
  }
}
