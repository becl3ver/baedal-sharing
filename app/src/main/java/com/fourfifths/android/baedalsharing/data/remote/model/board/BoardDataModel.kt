package com.fourfifths.android.baedalsharing.data.remote.model.board

import com.google.firebase.Timestamp

data class BoardDataModel(
    val title: String,
    val content: String,
    val category: Long,
    val uid: String,
    val nickname: String,
    val recommend: Long,
    val timestamp: Timestamp
)