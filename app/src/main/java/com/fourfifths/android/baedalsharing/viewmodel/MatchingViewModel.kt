package com.fourfifths.android.baedalsharing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingRequestDto
import com.fourfifths.android.baedalsharing.data.remote.repository.MatchingRepository
import kotlinx.coroutines.launch

class MatchingViewModel(private val repository: MatchingRepository) : ViewModel() {
    private val TAG = "MatchingViewModel"

    private val _matchingJoinResult = MutableLiveData<String>()
    val matchingJoinResult: LiveData<String> get() = _matchingJoinResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun setMatching(token: String, matchingRequestDto: MatchingRequestDto) {
        viewModelScope.launch {
            val response = repository.setMatching(token, matchingRequestDto)

            if (response.isSuccessful) {
                _matchingJoinResult.postValue(response.body())
            } else {
                _errorMessage.postValue(response.message())
            }
        }
    }
}