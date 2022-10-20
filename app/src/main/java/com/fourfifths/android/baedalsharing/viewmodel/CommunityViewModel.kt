package com.fourfifths.android.baedalsharing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fourfifths.android.baedalsharing.data.remote.model.board.Board
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CommunityViewModel : ViewModel() {
    private val _boards = MutableLiveData<ArrayList<Board>>()
    val boards: LiveData<ArrayList<Board>> get() = _boards

    private val db = FirebaseFirestore.getInstance()
    private var lastVisible: DocumentSnapshot? = null

    fun initBoards(category: Int) {
        val query = db.collection("Boards")
            .whereEqualTo("category", category)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20)
            .get()

        query.addOnSuccessListener { value ->
            if(value != null) {
                lastVisible = value.documents[value.documents.size - 1]

                val tmp = ArrayList<Board>()

                for (document in value.documents) {
                    tmp.add(convertToBoard(document.data!!))
                }

                _boards.value = tmp
            }
        }
    }

    fun loadMoreBoards(category: Int): Boolean {
        var flag = true

        val query = db.collection("Boards")
            .whereEqualTo("category", category)
            .orderBy("timestamp")
            .startAfter(lastVisible)
            .limit(20)
            .get()

        query.addOnSuccessListener { value ->
            if (value != null) {
                if(value.documents.size == 0) {
                    flag = false
                    return@addOnSuccessListener
                }

                lastVisible = value.documents[value.documents.size - 1]

                val tmp = ArrayList<Board>()

                for (document in value.documents) {
                    tmp.add(convertToBoard(document.data!!))
                }

                _boards.value = tmp
            }
        }

        return flag
    }

    private fun convertToBoard(data: Map<String, Any>): Board {
        return Board(
            data["title"] as String,
            data["content"] as String,
            data["category"] as Long,
            data["uid"] as String,
            data["nickname"] as String,
            data["recommend"] as Long,
            data["timestamp"] as Timestamp,
        )
    }
}