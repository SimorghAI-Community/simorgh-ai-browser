package com.simorgh.feature.browsercore

/**
 * Minimal context passed to BrowserCapability lifecycle callbacks.
 * Extended in later phases as real features need more data (e.g. tab id).
 */
data class PageContext(
    val url: String
)
