package com.fourfifths.android.baedalsharing.data.firebase.model.community

import com.google.firebase.Timestamp

data class CommentDataModel(
    val uid: String,
    val nickname: String,
    val timestamp: Timestamp,
    val content: String
)