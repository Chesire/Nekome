package com.chesire.malime.injection

import com.chesire.malime.util.IOScheduler
import com.chesire.malime.util.UIScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class UiModule {
    @Provides
    @UIScheduler
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @IOScheduler
    fun provideIOScheduler(): Scheduler = Schedulers.io()
}