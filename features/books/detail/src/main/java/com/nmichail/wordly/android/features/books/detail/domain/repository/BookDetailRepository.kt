package com.nmichail.wordly.android.features.books.detail.domain.repository

import com.nmichail.wordly.android.features.books.detail.domain.entity.BookDetail

interface BookDetailRepository {

	suspend fun getBookDetail(id: String): BookDetail
}