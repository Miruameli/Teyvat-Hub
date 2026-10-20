package com.teyvat.hub

import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.allowHardware
import com.teyvat.hub.data.DatabaseSeeder
import com.teyvat.hub.data.AppConstants
import dagger.hilt.android.HiltAndroidApp
import okio.Path.Companion.toOkioPath

/**
 * Application entry point that wires the Hilt dependency graph and seeds
 * offline content on first launch.
 *
 * A single shared instance keeps one Room database across the whole process
 * so that preferences and every repository observe the same data source.
 */
@HiltAndroidApp
class TeyvatHubApplication : Application(), SingletonImageLoader.Factory {

    @Inject
    lateinit var seeder: DatabaseSeeder

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Content must exist before the first screen reads it.
        appScope.launch { seeder.seedIfEmpty() }
    }

    /**
     * Memory-capped image loader.
     *
     * Character rows are ~13 KB but a full 2048px gacha portrait is 1.5 MB, so
     * the loader is bounded to keep bitmap heap usage predictable on low-RAM
     * devices. Rationale: docs/adr/0003-optimized-genshin-digital-assets.md
     */
    override fun newImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, AppConstants.IMAGE_MEMORY_CACHE_FRACTION)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache").toOkioPath())
                    .maxSizeBytes(AppConstants.IMAGE_DISK_CACHE_BYTES)
                    .build()
            }
            .allowHardware(false)
            .build()
}
