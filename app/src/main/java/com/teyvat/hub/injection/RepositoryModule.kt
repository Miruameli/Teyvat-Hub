package com.teyvat.hub.injection

import com.teyvat.hub.application.CharacterRepository
import com.teyvat.hub.application.ChecklistRepository
import com.teyvat.hub.application.LocalCharacterRepository
import com.teyvat.hub.application.LocalChecklistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Binds repository interfaces to their local implementations.
 *
 * Concrete classes use constructor injection; an interface still needs an
 * explicit @Binds so Hilt knows which implementation to hand callers.
 */
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    /** Local Room-backed roster is the only implementation for now. */
    @Binds
    fun bindCharacterRepository(
        implementation: LocalCharacterRepository,
    ): CharacterRepository

    /** Room-backed farming tasks. */
    @Binds
    fun bindChecklistRepository(
        implementation: LocalChecklistRepository,
    ): ChecklistRepository
}
