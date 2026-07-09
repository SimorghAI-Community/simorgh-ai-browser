package com.simorgh.feature.browsercore

/**
 * Contract every browser feature (AI, Adblock, Reader Mode, Persian rendering, ...)
 * must implement instead of touching WebView directly. See ARCHITECTURE.md section 4.2.
 */
interface BrowserCapability {
    val id: String
    fun onPageStarted(context: PageContext) {}
    fun onPageFinished(context: PageContext) {}
    fun onContentExtracted(content: PageContent) {}
    fun injectedScript(): String? = null
}
