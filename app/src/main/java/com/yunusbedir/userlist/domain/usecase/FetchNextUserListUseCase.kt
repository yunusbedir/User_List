package com.yunusbedir.userlist.domain.usecase

import com.yunusbedir.userlist.data.FetchError
import com.yunusbedir.userlist.data.FetchResponse
import com.yunusbedir.userlist.domain.UiState
import com.yunusbedir.userlist.domain.callback.RepositoryCallBack
import com.yunusbedir.userlist.domain.repository.UserListRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FetchNextUserListUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : BaseUseCase<Int>,
    RepositoryCallBack<FetchResponse, FetchError> {

    private val _sharedFlow = MutableStateFlow<UiState>(UiState.Loading)
    val sharedFlow: StateFlow<UiState> = _sharedFlow

    override suspend fun invoke(params: Int) {
        _sharedFlow.value = UiState.Loading
        userListRepository.fetchUserList(next = params.toString(), this@FetchNextUserListUseCase)
    }

    override fun success(result: FetchResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            _sharedFlow.value = UiState.Success(result)
        }
    }

    override fun failure(error: FetchError) {
        CoroutineScope(Dispatchers.IO).launch {
            _sharedFlow.value = UiState.Fail(error.errorDescription)
        }
    }
}