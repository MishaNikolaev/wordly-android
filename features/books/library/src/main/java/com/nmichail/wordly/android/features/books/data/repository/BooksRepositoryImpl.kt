package com.nmichail.wordly.android.features.books.data.repository

import com.nmichail.wordly.android.features.books.data.api.BooksApi
import com.nmichail.wordly.android.features.books.data.mapper.toEntity
import com.nmichail.wordly.android.features.books.domain.entity.BooksCatalog
import com.nmichail.wordly.android.features.books.domain.repository.BooksRepository
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val booksApi: BooksApi,
) : BooksRepository {

    override suspend fun getCatalog(): BooksCatalog =
        booksApi.getCatalog().toEntity()
}
