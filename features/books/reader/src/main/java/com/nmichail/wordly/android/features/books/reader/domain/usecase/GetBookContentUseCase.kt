package com.nmichail.wordly.android.features.books.reader.domain.usecase

import com.nmichail.wordly.android.features.books.reader.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.reader.domain.repository.BookReaderRepository
import javax.inject.Inject

class GetBookContentUseCase @Inject constructor(
    bookReaderRepository: BookReaderRepository,
) : suspend (String) -> BookContent by bookReaderRepository::getBookContent
