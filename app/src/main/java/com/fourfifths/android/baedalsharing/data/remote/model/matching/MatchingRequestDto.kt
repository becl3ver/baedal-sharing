package com.fourfifths.android.baedalsharing.data.remote.model.matching

data class MatchingRequestDto(
    val uid: String,
    val isCurrent: Boolean,
    val latitude: Double,
    val longitude: Double,
    val day: Int,
    val time: Int,
)