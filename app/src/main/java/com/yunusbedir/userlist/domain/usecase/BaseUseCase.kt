package com.yunusbedir.userlist.domain.usecase

import kotlinx.coroutines.flow.Flow

interface BaseUseCase<in PARAMS, out STATE> {

    suspend operator fun invoke(params: PARAMS): Flow<STATE>
}
