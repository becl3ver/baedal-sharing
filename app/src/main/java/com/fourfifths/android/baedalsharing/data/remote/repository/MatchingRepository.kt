package com.fourfifths.android.baedalsharing.data.remote.repository

import android.util.Log
import com.fourfifths.android.baedalsharing.data.remote.api.MatchingApi
import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingRequestDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchingRepository {
    private val TAG = MatchingRepository::class.java.simpleName

    fun setMatchingNow(token: String, matchingRequestDto: MatchingRequestDto): String {
        val call = MatchingApi.createApi().setMatchingNow(token, matchingRequestDto)
        var result: String = ""

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    result = response.body()!!
                } else {
                    Log.d(TAG, "failed with " + response.code().toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "failed with " + t.message)
            }
        })

        return result
    }
}