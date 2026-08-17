package com.nmichail.wordly.android.features.books.domain.repository

import com.nmichail.wordly.android.features.books.domain.entity.BooksCatalog

interface BooksRepository {

    suspend fun getCatalog(): BooksCatalog
}
