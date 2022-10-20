package com.fourfifths.android.baedalsharing.data.remote.api

import com.fourfifths.android.baedalsharing.BuildConfig
import com.fourfifths.android.baedalsharing.data.remote.service.MatchingService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object MatchingApi {
    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.MATCHING_API_URL)
        .client(client)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build()

    fun createApi(): MatchingService {
        return retrofit.create(MatchingService::class.java)
    }
}