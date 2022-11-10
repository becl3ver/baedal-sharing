package com.fourfifths.android.baedalsharing.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fourfifths.android.baedalsharing.data.firebase.model.community.PostDataModel
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import com.fourfifths.android.baedalsharing.view.App
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class NewPostActivity : ViewModel() {
    private val TAG = NewPostActivity::class.simpleName

    val boardTitle = MutableLiveData("")
    val boardContent = MutableLiveData("")

    private val _isEmptyEtExist = MutableLiveData(false)
    val isEmptyEtExist: LiveData<Boolean> get() = _isEmptyEtExist

    private val _submitResponse = MutableLiveData(0)
    val submitResponse: LiveData<Int> get() = _submitResponse

    var category = 0L

    fun pushNewPost() {
        val title = boardTitle.value!!
        val content = boardContent.value!!

        if (title.isEmpty() || content.isEmpty()) {
            _isEmptyEtExist.value = true
            return
        }

        val postDataModel = PostDataModel(
            title,
            content,
            category,
            FirebaseAuthUtils.getUid()!!,
            App.prefs.getNickname()!!,
            0L,
            Timestamp.now()
        )

        val db = FirebaseFirestore.getInstance()

        db.collection("Boards").add(postDataModel)
            .addOnSuccessListener {
                _submitResponse.value = 1
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "failed with $exception")
                _submitResponse.value = 2
            }
    }

    fun initFlags() {
        _isEmptyEtExist.value = false
        _submitResponse.value = 0
    }
}