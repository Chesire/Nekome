package com.chesire.malime.injection.modules

import com.chesire.malime.util.ComputationScheduler
import com.chesire.malime.util.IOScheduler
import com.chesire.malime.util.UIScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.schedulers.Schedulers

@Module
class UiModule {
    @Provides
    @ComputationScheduler
    @CheckReturnValue
    fun provideComputationScheduler(): Scheduler = Schedulers.computation()

    @Provides
    @UIScheduler
    @CheckReturnValue
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @IOScheduler
    @CheckReturnValue
    fun provideIOScheduler(): Scheduler = Schedulers.io()
}
