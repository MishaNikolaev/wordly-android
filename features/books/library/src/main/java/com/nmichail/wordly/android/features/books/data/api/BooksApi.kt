package com.nmichail.wordly.android.features.books.data.api

import com.nmichail.wordly.android.features.books.data.dto.BooksCatalogResponse
import retrofit2.http.GET

interface BooksApi {

    @GET("/api/gateway/books")
    suspend fun getCatalog(): BooksCatalogResponse
}
