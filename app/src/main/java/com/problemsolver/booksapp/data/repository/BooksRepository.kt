package com.problemsolver.booksapp.data.repository

import com.problemsolver.booksapp.data.model.BookDetail
import com.problemsolver.booksapp.data.model.WantToRead

interface BooksRepository {
    suspend fun getWantToRead(): WantToRead
    suspend fun getBookDetail(key: String): BookDetail
}