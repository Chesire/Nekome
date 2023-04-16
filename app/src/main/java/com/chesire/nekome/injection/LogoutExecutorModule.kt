package com.chesire.nekome.injection

import com.chesire.nekome.LogoutHandler
import com.chesire.nekome.app.settings.config.LogoutExecutor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LogoutExecutorModule {

    @Binds
    abstract fun bindExecutor(concrete: LogoutHandler): LogoutExecutor
}
