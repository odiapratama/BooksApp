package com.problemsolver.booksapp.data.repository

import com.problemsolver.booksapp.data.source.BooksApi
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    val booksApi: BooksApi
): BooksRepository {

    override suspend fun getWantToRead() = booksApi.getWantToRead()

    override suspend fun getBookDetail(key: String) = booksApi.getBookDetail(key)
}
