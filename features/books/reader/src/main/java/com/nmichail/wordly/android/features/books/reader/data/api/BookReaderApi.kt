package com.nmichail.wordly.android.features.books.reader.data.api

import com.nmichail.wordly.android.features.books.reader.data.dto.BookContentResponse
import com.nmichail.wordly.android.features.books.reader.data.dto.BookTranslationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BookReaderApi {

    @GET("/api/gateway/books/{id}")
    suspend fun getBookContent(@Path("id") id: String): BookContentResponse

    @GET("/api/gateway/books/{id}/translation")
    suspend fun getBookTranslation(@Path("id") id: String): BookTranslationResponse
}
