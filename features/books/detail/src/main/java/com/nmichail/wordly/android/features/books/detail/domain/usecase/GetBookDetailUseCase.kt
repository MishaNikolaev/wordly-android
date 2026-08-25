package com.nmichail.wordly.android.features.books.detail.domain.usecase

import com.nmichail.wordly.android.features.books.detail.domain.entity.BookDetail
import com.nmichail.wordly.android.features.books.detail.domain.repository.BookDetailRepository
import javax.inject.Inject

class GetBookDetailUseCase @Inject constructor(
	bookDetailRepository: BookDetailRepository,
) : suspend (String) -> BookDetail by bookDetailRepository::getBookDetail