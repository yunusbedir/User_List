package com.yunusbedir.userlist.domain

sealed class UiState {
    data class Success<T>(val data: T) : UiState()
    object Loading : UiState()
    data class Fail(val failureMessage: String) : UiState()

}