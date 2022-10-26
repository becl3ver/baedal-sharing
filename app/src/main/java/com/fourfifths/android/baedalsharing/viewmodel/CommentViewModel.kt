package com.fourfifths.android.baedalsharing.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fourfifths.android.baedalsharing.data.remote.model.board.CommentDataModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class CommentViewModel : ViewModel() {
    private val TAG = CommentViewModel::class.java.simpleName

    private val _comments = MutableLiveData<ArrayList<CommentDataModel>>()
    val comments: LiveData<ArrayList<CommentDataModel>> get() = _comments

    fun getComments(boardId: String) {
        val query = FirebaseFirestore.getInstance().collection("Boards")
            .document(boardId).collection("Comments").orderBy("timestamp").get()

        query.addOnSuccessListener { value ->
            val tmp = ArrayList<CommentDataModel>()

            for (snapshot in value) {
                tmp.add(
                    CommentDataModel(
                        snapshot.get("uid") as String,
                        snapshot.get("nickname") as String,
                        snapshot.get("timestamp") as Timestamp,
                        snapshot.get("content") as String
                    )
                )
            }

            _comments.value = tmp
        }.addOnFailureListener {
            Log.d(TAG, it.message.toString())
        }
    }
}