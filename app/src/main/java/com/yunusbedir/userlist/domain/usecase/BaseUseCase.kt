package com.yunusbedir.userlist.domain.usecase

import kotlinx.coroutines.flow.Flow

interface BaseUseCase<in PARAMS> {

    suspend operator fun invoke(params: PARAMS)
}
