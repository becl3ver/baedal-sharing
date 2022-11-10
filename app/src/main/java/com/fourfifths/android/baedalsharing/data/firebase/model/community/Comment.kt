package com.fourfifths.android.baedalsharing.data.firebase.model.community

data class Comment(
    val id: String,
    val uid: String,
    val nickname: String,
    val timestamp: com.google.firebase.Timestamp,
    val content: String
)
