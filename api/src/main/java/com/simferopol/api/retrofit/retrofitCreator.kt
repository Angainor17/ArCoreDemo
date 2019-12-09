package com.simferopol.api.retrofit

import com.google.gson.GsonBuilder
import com.simferopol.api.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT_SECONDS = 60L

/**
 * @param baseUrl Base Url
 * @return Retrofit with Auth Interceptor
 */
fun createRetrofit(baseUrl: String): Retrofit {
    return getDefaultAuthRetrofit(baseUrl, getDefaultOkHttpClientBuilder())
}

/*** Получение Interceptor логирования запросов в сеть */
fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.HEADERS
    } else HttpLoggingInterceptor.Level.BASIC
    return logging
}

/**
 * @param baseUrl      Base URL
 * @param okHttpClient OkHttpClient object
 * @return Retrofit Instance without Auth
 */
private fun getDefaultAuthRetrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient.Builder
): Retrofit {
    return getDefaultRetrofitBuilder(baseUrl).client(
        okHttpClient
            .addInterceptor(getHttpLoggingInterceptor())
            .build()
    )
        .build()
}

/**
 * @param baseUrl Base Url
 * @return Retrofit Builder with Gson and RxJava
 */
private fun getDefaultRetrofitBuilder(baseUrl: String): Retrofit.Builder = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))

/*** @return OkHttpClient with connection Params */
private fun getDefaultOkHttpClientBuilder() = OkHttpClient.Builder()
    .retryOnConnectionFailure(true)
    .readTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
    .writeTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
    .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
