package com.yunusbedir.userlist.ui.main

import com.yunusbedir.userlist.data.FetchResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunusbedir.userlist.domain.UiState
import com.yunusbedir.userlist.domain.usecase.FetchUserListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchUserListUseCase: FetchUserListUseCase
) : ViewModel() {

    private val _userListLiveData = MutableSharedFlow<UiState>()
    val userListLiveData: SharedFlow<UiState> = _userListLiveData

    private var nextUserList: Int = 0

    fun nextUserList() {
        viewModelScope.launch {
            fetchUserListUseCase.invoke(nextUserList).collect {
                if (it is UiState.Success<*>) {
                    nextUserList = ((it.data as FetchResponse).next ?: "0").toInt()
                }
                _userListLiveData.emit(it)
            }
        }
    }
}