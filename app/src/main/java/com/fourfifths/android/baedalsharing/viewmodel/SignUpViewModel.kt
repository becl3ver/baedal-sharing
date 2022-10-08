package com.fourfifths.android.baedalsharing.viewmodel

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    private val TAG = SignUpViewModel::class.java.simpleName

    val nickname = MutableLiveData("")
    val isAgreed = MutableLiveData(false)

    val noticeColor = Transformations.switchMap(nickname) {
        MutableLiveData(if(it.length >= 2) Color.GREEN else Color.RED)
    }

    val noticeText = Transformations.switchMap(nickname) {
        MutableLiveData(if (it.length >= 2) "사용 가능한 닉네임입니다." else "2글자 이상 입력해주세요.")
    }

    val acceptButton = Transformations.switchMap(nickname) {
        MutableLiveData(it.length >= 2)
    }
}