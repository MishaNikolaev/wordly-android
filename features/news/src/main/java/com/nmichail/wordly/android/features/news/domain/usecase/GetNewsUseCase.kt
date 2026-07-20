package com.nmichail.wordly.android.features.news.domain.usecase

import com.nmichail.wordly.android.features.news.domain.entity.News
import com.nmichail.wordly.android.features.news.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
	newsRepository: NewsRepository,
) : suspend (String) -> News by newsRepository::getNews