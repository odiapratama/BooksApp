package com.problemsolver.booksapp.di

import com.problemsolver.booksapp.data.repository.BooksRepository
import com.problemsolver.booksapp.data.repository.BooksRepositoryImpl
import com.problemsolver.booksapp.data.source.BooksApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideBookRepository(booksApi: BooksApi): BooksRepository {
        return BooksRepositoryImpl(booksApi)
    }
}