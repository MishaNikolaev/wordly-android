package com.nmichail.wordly.android.features.books.domain.usecase

import com.nmichail.wordly.android.features.books.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.domain.repository.BooksRepository
import javax.inject.Inject

class GetBookTranslationUseCase @Inject constructor(
    booksRepository: BooksRepository,
) : suspend (String) -> BookTranslation by booksRepository::getBookTranslation