package com.fourfifths.android.baedalsharing.data.firebase.model.community

import java.io.Serializable

data class Post(
    val id: String,
    val title: String,
    val content: String,
    val category: Long,
    val uid: String,
    val nickname: String,
    val recommend: Long,
    var date: String
) : Serializable
