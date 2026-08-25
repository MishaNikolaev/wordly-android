package com.nmichail.wordly.android.features.books.detail.data.repository

import com.nmichail.wordly.android.features.books.detail.data.api.BookDetailApi
import com.nmichail.wordly.android.features.books.detail.data.mapper.toDomain
import com.nmichail.wordly.android.features.books.detail.domain.entity.BookDetail
import com.nmichail.wordly.android.features.books.detail.domain.repository.BookDetailRepository
import javax.inject.Inject

class BookDetailRepositoryImpl @Inject constructor(
	private val bookDetailApi: BookDetailApi,
) : BookDetailRepository {

	override suspend fun getBookDetail(id: String): BookDetail =
		bookDetailApi.getBookDetail(id = id).toDomain()
}