package com.fourfifths.android.baedalsharing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fourfifths.android.baedalsharing.data.remote.model.board.Board
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel : ViewModel() {
    private val _boards = MutableLiveData<ArrayList<Board>>()
    private val db = FirebaseFirestore.getInstance()
    private var lastVisible: DocumentSnapshot? = null
    val boards: LiveData<ArrayList<Board>> get() = _boards

    fun initBoards() {
        db.collection("Boards")
            .whereEqualTo("category", 1)
            .orderBy("timestamp")
            .limit(20)
            .addSnapshotListener { value, _ ->
                if (value != null) {
                    lastVisible = value.documents[value.documents.size - 1]

                    for (snapshot in value.documents) {
                        _boards.value!!.add(convertToBoard(snapshot))
                    }
                }
            }
    }

    fun loadBoards() {
        db.collection("Boards")
            .whereEqualTo("category", 1)
            .orderBy("timestamp")
            .startAfter(lastVisible)
            .limit(20)
            .addSnapshotListener { value, _ ->
                if(value != null) {
                    lastVisible = value.documents[value.documents.size - 1]

                    for (snapshot in value.documents) {
                        _boards.value!!.add(convertToBoard(snapshot))
                    }
                }
            }
    }

    private fun convertToBoard(snapshot: DocumentSnapshot): Board {
        return Board(
            snapshot.data!!["title"] as String,
            snapshot.data!!["content"] as String,
            snapshot.data!!["nickname"] as String,
            snapshot.data!!["uid"] as String,
            snapshot.data!!["recommend"] as Int,
            snapshot.data!!["timestamp"] as Long
        )
    }

}