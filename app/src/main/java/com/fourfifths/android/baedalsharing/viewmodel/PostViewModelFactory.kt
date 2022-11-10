package com.fourfifths.android.baedalsharing.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PostViewModelFactory(private val postId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            return CommentViewModel(postId) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}