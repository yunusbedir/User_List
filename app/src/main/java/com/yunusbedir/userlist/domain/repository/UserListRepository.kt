package com.yunusbedir.userlist.domain.repository

import com.yunusbedir.userlist.data.FetchError
import com.yunusbedir.userlist.data.FetchResponse
import com.yunusbedir.userlist.domain.callback.RepositoryCallBack

interface UserListRepository {
    fun fetchUserList(
        next: String?,
        repositoryCallBack: RepositoryCallBack<FetchResponse, FetchError>
    )
}