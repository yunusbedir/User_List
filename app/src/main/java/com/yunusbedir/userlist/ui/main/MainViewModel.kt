package com.yunusbedir.userlist.ui.main

import com.yunusbedir.userlist.data.FetchResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunusbedir.userlist.data.Person
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

    private val _userListSharedFlow = MutableSharedFlow<UiState>()
    val userListSharedFlow: SharedFlow<UiState> = _userListSharedFlow

    private var currentList: List<Person>? = null
    private var nextUserList: Int = 0

    init {
        viewModelScope.launch {
                fetchUserListUseCase.resultStateFlow.collect {
                    if (it is UiState.Success<*>) {
                        nextUserList = ((it.data as FetchResponse).next ?: "0").toInt()
                        val userList = ArrayList<Person>()
                        currentList?.let { it1 -> userList.addAll(it1) }
                        userList.addAll(it.data.people)
                        _userListSharedFlow.emit(UiState.Success(userList))
                    } else {
                        _userListSharedFlow.emit(it)
                    }
                }
        }
    }

    fun nextUserList(currentList: List<Person>) {
        viewModelScope.launch {
            this@MainViewModel.currentList = currentList
            fetchUserListUseCase.invoke(nextUserList)
        }
    }

    fun getUserList() {
        viewModelScope.launch {
            nextUserList = 0
            this@MainViewModel.currentList = null
            fetchUserListUseCase.invoke(null)
        }
    }
}