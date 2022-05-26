package com.yunusbedir.userlist.ui.main

import com.yunusbedir.userlist.data.FetchResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunusbedir.userlist.data.Person
import com.yunusbedir.userlist.domain.UiState
import com.yunusbedir.userlist.domain.usecase.FetchNextUserListUseCase
import com.yunusbedir.userlist.domain.usecase.FetchUserListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchUserListUseCase: FetchUserListUseCase,
    private val fetchNextUserListUseCase: FetchNextUserListUseCase
) : ViewModel() {

    private val _userListLiveData = MutableSharedFlow<UiState>()
    val userListLiveData: SharedFlow<UiState> = _userListLiveData

    private var nextUserList: Int = 0

    init {
        viewModelScope.launch {
            launch {

                fetchUserListUseCase.sharedFlow.collect {
                    if (it is UiState.Success<*>) {
                        nextUserList = ((it.data as FetchResponse).next ?: "0").toInt()
                        _userListLiveData.emit(UiState.Success(it.data.people))
                    } else {
                        _userListLiveData.emit(it)
                    }
                }
            }
        }
    }

    fun nextUserList(currentList: List<Person>) {
        viewModelScope.launch {
            fetchNextUserListUseCase.invoke(nextUserList)
            fetchNextUserListUseCase.sharedFlow.collect {
                when (it) {
                    is UiState.Success<*> -> {
                        nextUserList = ((it.data as FetchResponse).next ?: "0").toInt()

                        val userList = ArrayList<Person>()
                        userList.addAll(currentList)
                        userList.addAll(it.data.people)
                        _userListLiveData.emit(UiState.Success(userList))
                    }
                    else -> {
                        _userListLiveData.emit(it)
                    }
                }
            }
        }
    }

    fun getUserList() {
        viewModelScope.launch {
            nextUserList = 0
            fetchUserListUseCase.invoke(nextUserList)
        }
    }
}