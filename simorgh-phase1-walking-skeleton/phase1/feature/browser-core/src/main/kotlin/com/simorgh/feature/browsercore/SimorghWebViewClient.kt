package com.simorgh.feature.browsercore

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Bridges real WebView lifecycle callbacks to the CapabilityRegistry.
 * This is the only place that should talk to WebView directly for
 * page-lifecycle purposes — capabilities never touch WebView themselves.
 */
class SimorghWebViewClient(
    private val registry: CapabilityRegistry
) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        url?.let { registry.notifyPageStarted(PageContext(it)) }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        url?.let { registry.notifyPageFinished(PageContext(it)) }
    }
}
