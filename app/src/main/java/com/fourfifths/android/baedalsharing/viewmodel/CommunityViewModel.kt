package com.fourfifths.android.baedalsharing.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fourfifths.android.baedalsharing.data.remote.model.board.Board
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommunityViewModel : ViewModel() {
    private val TAG = CommunityViewModel::class.java.simpleName
    private val LOADING_LIMIT = 5L

    private val _boards = MutableLiveData<ArrayList<Board>>()
    val boards: LiveData<ArrayList<Board>> get() = _boards

    private val _isLastItemVisible = MutableLiveData(false)
    val isLastItemVisible: LiveData<Boolean> get() = _isLastItemVisible

    private val db = FirebaseFirestore.getInstance()
    private var lastVisible: DocumentSnapshot? = null

    fun initBoards(category: Int) {
        val query = if (category == 0) {
            db.collection("Boards")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(LOADING_LIMIT)
                .get()
        } else {
            db.collection("Boards")
                .whereEqualTo("category", 1)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(LOADING_LIMIT)
                .get()
        }

        query.addOnSuccessListener { value ->
            if (value != null && value.documents.size != 0) {
                lastVisible = value.documents[value.documents.size - 1]

                val tmp = ArrayList<Board>()

                for (document in value.documents) {
                    Log.d(TAG, document.id)
                    tmp.add(convertToBoard(document.id, document.data!!))
                }

                _boards.value = tmp
            }
        }
    }

    fun loadMoreBoards(category: Int) {
        if (lastVisible == null) {
            return
        }

        val query = if (category == 0) {
            db.collection("Boards")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereLessThan("timestamp", (lastVisible!!.data?.get("timestamp") as Timestamp))
                .limit(LOADING_LIMIT)
                .get()
        } else {
            db.collection("Boards")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("category", 1)
                .whereLessThan("timestamp", (lastVisible!!.data?.get("timestamp") as Timestamp))
                .startAfter(lastVisible)
                .limit(LOADING_LIMIT)
                .get()
        }

        query.addOnSuccessListener { value ->
            if (value != null) {
                if (value.documents.size == 0) {
                    _isLastItemVisible.value = true
                    return@addOnSuccessListener
                }

                lastVisible = value.documents[value.documents.size - 1]

                val tmp = ArrayList<Board>()

                for (document in value.documents) {
                    Log.d(TAG, document.id)
                    tmp.add(convertToBoard(document.id, document.data!!))
                }

                _boards.value = tmp
            }
        }
    }

    private fun convertToBoard(id: String, data: Map<String, Any>): Board {
        return Board(
            id,
            data["title"] as String,
            data["content"] as String,
            data["category"] as Long,
            data["uid"] as String,
            data["nickname"] as String,
            data["recommend"] as Long,
            (data["timestamp"] as Timestamp).toDate().let {
                SimpleDateFormat(
                    "yyyy/MM/dd HH:mm",
                    Locale.getDefault()
                ).format(it)
            }
        )
    }
}