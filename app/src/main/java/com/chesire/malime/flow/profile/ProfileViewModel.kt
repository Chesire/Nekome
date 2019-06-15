package com.chesire.malime.flow.profile

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chesire.malime.repo.SeriesRepository
import com.chesire.malime.repo.UserRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    seriesRepository: SeriesRepository,
    userRepository: UserRepository
) : ViewModel() {
    val user = userRepository.user
    val anime = seriesRepository.anime
    val manga = seriesRepository.manga

    val userName = Transformations.map(user) { it.name }
}
