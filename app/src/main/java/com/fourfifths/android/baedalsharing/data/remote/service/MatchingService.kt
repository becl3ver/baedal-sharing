package com.fourfifths.android.baedalsharing.data.remote.service

import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingRequestDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT

interface MatchingService {
    @PUT("/matching/current")
    fun setMatchingNow(
        @Header("Authorization") token: String,
        @Body matchingRequestDto: MatchingRequestDto
    ): Call<String>

    @PUT("/matching/reservation")
    fun setReservation(
        @Header("Authorization") token: String,
        @Body matchingRequestDto: MatchingRequestDto
    ): Call<String>

    @PUT("/matching/cancel")
    fun deleteMatching(@Header("Authorization") token: String): Call<String>
}