package com.fourfifths.android.baedalsharing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fourfifths.android.baedalsharing.data.remote.model.board.Board
import com.fourfifths.android.baedalsharing.utils.FirebaseRef
import com.google.firebase.Timestamp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.snapshot.Node
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommunityViewModel : ViewModel() {
    private val _boards = MutableLiveData<ArrayList<Board>>()
    val boards: LiveData<ArrayList<Board>> get() = _boards

    private val db = FirebaseFirestore.getInstance()
    private var lastVisible: DocumentSnapshot? = null

    private var visible: Timestamp? = null

    fun initBoards(category: Int) {
        val query = db.collection("Boards").let {
            if(category != 0) {
                it.whereEqualTo("category", category)
            }
            it.orderBy("timestamp", Query.Direction.DESCENDING)
            it.limit(6)
            it.get()
        }

        query.addOnSuccessListener { value ->
            if(value != null && value.documents.size != 0) {
                lastVisible = value.documents[value.documents.size - 1]

                val tmp = ArrayList<Board>()

                for (document in value.documents) {
                    tmp.add(convertToBoard(document.id, document.data!!))
                }

                _boards.value = tmp
            }
        }
    }

    fun loadMoreBoards(category: Int): Boolean {
        var flag = true

        val query = db.collection("Boards").let {
            if (category != 0) {
                it.whereEqualTo("category", category)
            }
            it.orderBy("timestamp")
            it.startAfter(lastVisible)
            it.limit(6)
            it.get()
        }

        query.addOnSuccessListener { value ->
            if (value != null) {
                if (value.documents.size == 0) {
                    flag = false
                    return@addOnSuccessListener
                }

                lastVisible = value.documents[value.documents.size - 1]

                val tmp = ArrayList<Board>()

                for (document in value.documents) {
                    tmp.add(convertToBoard(document.id, document.data!!))
                }

                _boards.value = tmp
            }
        }

        return flag
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