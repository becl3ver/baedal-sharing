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
}