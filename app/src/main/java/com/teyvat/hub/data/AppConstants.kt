package com.teyvat.hub.data

/** Constants shared across layers so magic values live in one place. */
object AppConstants {

    /** Arena for images: bytes reserved on disk for avatar cache. */
    const val IMAGE_DISK_CACHE_BYTES = 64L * 1024L * 1024L

    /** Fraction of app heap the image memory cache may use. */
    const val IMAGE_MEMORY_CACHE_FRACTION = 0.12

    /** Search returns no more than this many hits. */
    const val SEARCH_RESULT_LIMIT = 20
}
