package com.simorgh.feature.browsercore

/**
 * Holds all registered BrowserCapability instances and calls them
 * at the right points in the WebView page lifecycle.
 *
 * Phase 1: capabilities are registered manually (see MainActivity).
 * DI-based registration (Hilt @IntoSet) is introduced in a later phase.
 */
class CapabilityRegistry {

    private val capabilities = mutableListOf<BrowserCapability>()

    fun register(capability: BrowserCapability) {
        capabilities += capability
    }

    fun notifyPageStarted(context: PageContext) {
        capabilities.forEach { it.onPageStarted(context) }
    }

    fun notifyPageFinished(context: PageContext) {
        capabilities.forEach { it.onPageFinished(context) }
    }

    fun notifyContentExtracted(content: PageContent) {
        capabilities.forEach { it.onContentExtracted(content) }
    }
}
