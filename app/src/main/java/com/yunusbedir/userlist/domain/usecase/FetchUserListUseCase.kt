package com.yunusbedir.userlist.domain.usecase

import com.yunusbedir.userlist.data.FetchError
import com.yunusbedir.userlist.data.FetchResponse
import com.yunusbedir.userlist.domain.UiState
import com.yunusbedir.userlist.domain.callback.RepositoryCallBack
import com.yunusbedir.userlist.domain.repository.UserListRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FetchUserListUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : BaseUseCase<Int, UiState>,
    RepositoryCallBack<FetchResponse, FetchError> {

    private val _sharedFlow = MutableSharedFlow<UiState>()

    override suspend fun invoke(params: Int): SharedFlow<UiState> {
        _sharedFlow.emit(UiState.Loading)
        userListRepository.fetchUserList(next = params.toString(), this@FetchUserListUseCase)
        return _sharedFlow.asSharedFlow()
    }

    override fun success(result: FetchResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            _sharedFlow.emit(UiState.Success(result))
        }
    }

    override fun failure(error: FetchError) {
        CoroutineScope(Dispatchers.IO).launch {
            _sharedFlow.emit(UiState.Fail(error.errorDescription))
        }
    }
}