package com.yunusbedir.userlist.domain.callback

interface RepositoryCallBack<RESULT, ERROR> {
    fun success(result: RESULT)
    fun failure(error: ERROR)
}