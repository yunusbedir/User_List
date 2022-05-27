package com.yunusbedir.userlist.data.repository

import com.yunusbedir.userlist.data.DataSource
import com.yunusbedir.userlist.data.FetchError
import com.yunusbedir.userlist.data.FetchResponse
import com.yunusbedir.userlist.domain.callback.RepositoryCallBack
import com.yunusbedir.userlist.domain.repository.UserListRepository
import javax.inject.Inject

class UserListRepositoryImpl @Inject constructor(
    private val dataSource: DataSource
) : UserListRepository {

    override fun fetchUserList(
        next: String?,
        repositoryCallBack: RepositoryCallBack<FetchResponse, FetchError>
    ) {
        dataSource.fetch(next) { data, error ->
            if (data != null) {
                if (data.people.isNullOrEmpty())
                    repositoryCallBack.failure(FetchError(errorDescription = "Can not found user list. Please try again!"))
                else
                    repositoryCallBack.success(data)
            } else {
                error?.let { repositoryCallBack.failure(it) }
            }
        }
    }

}