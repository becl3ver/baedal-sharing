package com.fourfifths.android.baedalsharing.data.remote.service

import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingInfoDto
import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingRequestDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface MatchingService {
    @PUT("/matching/current")
    fun setMatching(
        @Header("Authorization") token: String,
        @Body matchingRequestDto: MatchingRequestDto
    ): Call<String>

    @GET("/matching/history")
    fun getHistory(@Header("Authorization") token: String): Call<MatchingInfoDto>

    @FormUrlEncoded
    @PUT("/matching/cancel")
    fun cancelMatching(@Header("Authorization") token: String, @Field("matching_id") matchingId: Long): Call<String>
}