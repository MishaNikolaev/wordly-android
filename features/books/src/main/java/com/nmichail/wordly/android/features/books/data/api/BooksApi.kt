package com.nmichail.wordly.android.features.books.data.api

import com.nmichail.wordly.android.features.books.data.dto.BookContentResponse
import com.nmichail.wordly.android.features.books.data.dto.BookTranslationResponse
import com.nmichail.wordly.android.features.books.data.dto.BooksCatalogResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BooksApi {

    @GET("/api/gateway/books")
    suspend fun getCatalog(): BooksCatalogResponse

    @GET("/api/gateway/books/{id}")
    suspend fun getBookContent(@Path("id") id: String): BookContentResponse

    @GET("/api/gateway/books/{id}/translation")
    suspend fun getBookTranslation(@Path("id") id: String): BookTranslationResponse
}