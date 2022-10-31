package com.fourfifths.android.baedalsharing.data.remote.service

import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingInfoDto
import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingRequestDto
import retrofit2.Response
import retrofit2.http.*

interface MatchingService {
    @POST("/matching/current")
    suspend fun setMatching(
        @Header("Authorization") token: String,
        @Body matchingRequestDto: MatchingRequestDto
    ): Response<String>

    @GET("/matching/history")
    suspend fun getHistory(@Header("Authorization") token: String): Response<MatchingInfoDto>

    @FormUrlEncoded
    @POST("/matching/cancel")
    suspend fun cancelMatching(
        @Header("Authorization") token: String,
        @Body matchingId: Long
    ): Response<String>
}