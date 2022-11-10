package com.fourfifths.android.baedalsharing.data.firebase.model.community

import com.google.firebase.Timestamp

data class PostDataModel(
    val title: String,
    val content: String,
    val category: Long,
    val uid: String,
    val nickname: String,
    val recommend: Long,
    val timestamp: Timestamp
)