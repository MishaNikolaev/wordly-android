package com.nmichail.wordly.android.features.books.domain.usecase

import com.nmichail.wordly.android.features.books.domain.entity.BooksCatalog
import com.nmichail.wordly.android.features.books.domain.repository.BooksRepository
import javax.inject.Inject

class GetBooksCatalogUseCase @Inject constructor(
	booksRepository: BooksRepository,
) : suspend () -> BooksCatalog by booksRepository::getCatalog