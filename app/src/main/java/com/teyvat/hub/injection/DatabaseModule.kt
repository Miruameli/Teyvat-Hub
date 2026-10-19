package com.teyvat.hub.injection

import android.content.Context
import androidx.room.Room
import com.teyvat.hub.data.TeyvatHubDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Provides the single local database instance for the app process. */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): TeyvatHubDatabase = Room
        .databaseBuilder(context, TeyvatHubDatabase::class.java, TeyvatHubDatabase.NAME)
        .build()
}
