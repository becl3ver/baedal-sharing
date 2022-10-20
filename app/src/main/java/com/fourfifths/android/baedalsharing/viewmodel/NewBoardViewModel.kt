package com.fourfifths.android.baedalsharing.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fourfifths.android.baedalsharing.data.remote.model.board.BoardDataModel
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import com.google.firebase.firestore.FirebaseFirestore

class NewBoardViewModel : ViewModel() {
    private val TAG = NewBoardViewModel::class.java.simpleName

    val boardTitle = MutableLiveData("")
    val boardContent = MutableLiveData("")

    /*private fun pushBoard(category: Int) {
        val title = boardTitle.value
        val content = boardContent.value

        if(title == null || content == null) {
            // TODO : 액티비티에서 observe 해서 Toast 띄우기
            return
        }

        val boardDataModel = BoardDataModel(
            boardTitle.value!!,
            boardContent.value!!,
            category,
            FirebaseAuthUtils.getUid()!!
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("Boards").add(boardDataModel)
    }*/
}