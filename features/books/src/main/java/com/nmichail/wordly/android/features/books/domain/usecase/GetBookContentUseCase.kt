package com.nmichail.wordly.android.features.books.domain.usecase

import com.nmichail.wordly.android.features.books.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.domain.repository.BooksRepository
import javax.inject.Inject

class GetBookContentUseCase @Inject constructor(
	booksRepository: BooksRepository,
) : suspend (String) -> BookContent by booksRepository::getBookContent