package com.nmichail.wordly.android.features.books.reader.domain.usecase

import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.reader.domain.repository.BookReaderRepository
import javax.inject.Inject

class GetBookTranslationUseCase @Inject constructor(
    bookReaderRepository: BookReaderRepository,
) : suspend (String) -> BookTranslation by bookReaderRepository::getBookTranslation
