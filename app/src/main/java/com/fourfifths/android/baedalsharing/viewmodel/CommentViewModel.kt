package com.fourfifths.android.baedalsharing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fourfifths.android.baedalsharing.data.firebase.model.community.Comment
import com.fourfifths.android.baedalsharing.data.firebase.repository.CommentRepository

class CommentViewModel(private val postId: String) : ViewModel() {
    private val TAG = CommentViewModel::class.java.simpleName
    private val repository: CommentRepository by lazy { CommentRepository() }

    private val _comments = MutableLiveData<MutableList<Comment>>()
    val comments: LiveData<MutableList<Comment>> get() = _comments

    fun getComments() {
        repository.getComments(_comments, postId)
    }
}