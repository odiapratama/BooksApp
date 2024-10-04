package com.problemsolver.booksapp.data.source

import com.problemsolver.booksapp.data.model.BookDetail
import com.problemsolver.booksapp.data.model.WantToRead
import retrofit2.http.GET
import retrofit2.http.Path

interface BooksApi {

    @GET("people/mekBot/books/want-to-read.json")
    suspend fun getWantToRead(): WantToRead

    @GET("{key}.json")
    suspend fun getBookDetail(
        @Path("key") key: String
    ): BookDetail
}