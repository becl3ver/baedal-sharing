package com.fourfifths.android.baedalsharing.data.remote.model.board

data class Board(
    val title: String,
    val content: String,
    val nickname: String,
    val uid: String,
    val recommend: Int,
    val timestamp: Long
)
