package com.learnupp.ui.video

/**
 * Best-effort prefetch of a video to reduce initial startup latency.
 * Implementations may buffer a small portion in memory.
 */
expect fun prefetchVideo(url: String)


