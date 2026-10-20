package com.teyvat.hub.injection

import javax.inject.Named
import javax.inject.Singleton
import com.teyvat.hub.domain.checklist.ChecklistResetCalculator
import com.teyvat.hub.domain.team.TeamSynergyAnalyzer
import com.teyvat.hub.domain.team.TeamValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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

    @Provides
    @Named("timeProvider")
    @Singleton
    fun provideTimeProvider(): () -> Long = { System.currentTimeMillis() }

    /** Stateless team rules; shared so the screen and ViewModel agree. */
    @Provides
    @Singleton
    fun provideTeamValidator(): TeamValidator = TeamValidator()

    @Provides
    @Singleton
    fun provideTeamSynergyAnalyzer(): TeamSynergyAnalyzer = TeamSynergyAnalyzer()
}
