package com.fourfifths.android.baedalsharing.data.remote.model.matching

import com.google.firebase.Timestamp

data class MatchingInfo(
    val isFinished: Boolean,
    val menu: Int,
    val timestamp: Timestamp,
    val requestId: Long
)