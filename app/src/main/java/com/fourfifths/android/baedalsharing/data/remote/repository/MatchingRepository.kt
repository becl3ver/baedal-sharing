package com.fourfifths.android.baedalsharing.data.remote.repository

import android.util.Log
import com.fourfifths.android.baedalsharing.data.remote.api.MatchingApi
import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingInfo
import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingInfoDto
import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingRequestDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchingRepository {
    private val TAG = MatchingRepository::class.java.simpleName

    fun setMatching(token: String, matchingRequestDto: MatchingRequestDto): String {
        val call = MatchingApi.createApi().setMatching(token, matchingRequestDto)
        var result: String = ""

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    result = response.body()!!
                } else {
                    Log.d(TAG, "failed with ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "failed with ${t.message}")
            }
        })

        return result
    }

    fun getHistory(token: String): ArrayList<MatchingInfo> {
        val call = MatchingApi.createApi().getHistory(token)
        var result = ArrayList<MatchingInfo>()

        call.enqueue(object : Callback<MatchingInfoDto> {
            override fun onResponse(call: Call<MatchingInfoDto>, response: Response<MatchingInfoDto>) {
                if(response.isSuccessful) {
                    for(data in response.body()!!.data) {
                        result.add(data)
                    }
                } else {
                    Log.d(TAG, "failed with ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MatchingInfoDto>, t: Throwable) {
                Log.d(TAG, "failed with ${t.message}")
            }
        })

        return result
    }

    fun cancelMatching(token: String, matchingId: Long): String {
        val call = MatchingApi.createApi().cancelMatching(token, matchingId)
        var result = ""

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    result = response.body()!!
                } else {
                    Log.d(TAG, "failed with ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "failed with ${t.message}")
            }
        })

        return result
    }
}