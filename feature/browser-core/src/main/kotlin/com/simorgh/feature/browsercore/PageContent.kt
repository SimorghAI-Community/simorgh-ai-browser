package com.simorgh.feature.browsercore

/**
 * Placeholder for extracted page content.
 * Real extraction (title, text, metadata) is added when a consumer
 * (e.g. feature:persian-intelligence) actually needs it.
 */
data class PageContent(
    val url: String,
    val title: String? = null
)
