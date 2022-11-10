package com.fourfifths.android.baedalsharing.viewmodel

import android.graphics.Color
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fourfifths.android.baedalsharing.R

class SignUpViewModel : ViewModel() {
    private val TAG = SignUpViewModel::class.java.simpleName

    val nickname = MutableLiveData("")
    val isAgreed = MutableLiveData(false)

    private val validLevel = Transformations.switchMap(nickname) {
        MutableLiveData(checkNicknameValidLevel(nickname.value!!))
    }

    val noticeColor = Transformations.switchMap(validLevel) {
        MutableLiveData(
            when(it.value) {
                in 0..2 -> Color.RED
                else -> Color.GREEN
            }
        )
    }

    val noticeText = Transformations.switchMap(validLevel) {
        MutableLiveData(
            when(it.value) {
                0 -> ""
                1 -> "닉네임의 길이는 2 ~ 10글자여야 합니다."
                2 -> "한글, 영문 외의 문자는 사용할 수 없습니다."
                else -> "사용 가능한 닉네임입니다."
            }
        )
    }

    val acceptButton = Transformations.switchMap(validLevel) {
        MutableLiveData(it.value == 3)
    }

    private fun checkNicknameValidLevel(nickname: String): MutableLiveData<Int> {
        return MutableLiveData(
            if(nickname.isEmpty()) {
                0
            }
            else if (nickname.length < 2 || nickname.length > 10) {
                1
            } else if (!isSatisfyingRegex(nickname)) {
                2
            } else {
                3
            }
        )
    }

    private fun isSatisfyingRegex(nickname: String): Boolean {
        val regex = Regex("^[ㄱ-ㅣ가-힣a-zA-Z]{2,10}\$")
        return regex.matches(nickname)
    }
}