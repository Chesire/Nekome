package com.chesire.malime.flow.profile

import androidx.lifecycle.ViewModel
import com.chesire.malime.repo.SeriesRepository
import com.chesire.malime.repo.UserRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val seriesRepository: SeriesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

}
