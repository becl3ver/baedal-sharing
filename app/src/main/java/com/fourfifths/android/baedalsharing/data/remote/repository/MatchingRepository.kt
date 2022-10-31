package com.fourfifths.android.baedalsharing.data.remote.repository

import com.fourfifths.android.baedalsharing.data.remote.api.MatchingApi
import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingRequestDto

class MatchingRepository {
    private val TAG = MatchingRepository::class.simpleName

    suspend fun setMatching(token: String, matchingRequestDto: MatchingRequestDto) =
        MatchingApi.createApi().setMatching(token, matchingRequestDto)

    suspend fun getHistory(token: String) = MatchingApi.createApi().getHistory(token)

    suspend fun cancelMatching(token: String, matchingId: Long) =
        MatchingApi.createApi().cancelMatching(token, matchingId)
}