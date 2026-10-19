package com.teyvat.hub.injection

import com.teyvat.hub.domain.service.ChecklistResetCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Supplies domain services that carry non-trivial constructor arguments.
 *
 * The reset calculator owns a timezone and reset hour, so it is created here
 * rather than injected; that keeps ViewModels free of timezone concerns.
 */
@Module
@InstallIn(SingletonComponent::class)
object DomainServiceModule {

    @Provides
    @Singleton
    fun provideChecklistResetCalculator(): ChecklistResetCalculator =
        ChecklistResetCalculator()
}
