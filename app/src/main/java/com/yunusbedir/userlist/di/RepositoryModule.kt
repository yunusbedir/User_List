package com.yunusbedir.userlist.di

import com.yunusbedir.userlist.data.repository.UserListRepositoryImpl
import com.yunusbedir.userlist.domain.repository.UserListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideProductRepository(userListRepository: UserListRepositoryImpl): UserListRepository

}