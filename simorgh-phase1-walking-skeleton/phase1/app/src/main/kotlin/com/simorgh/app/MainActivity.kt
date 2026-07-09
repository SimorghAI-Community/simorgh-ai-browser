package com.simorgh.app

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import com.simorgh.feature.browsercore.CapabilityRegistry
import com.simorgh.feature.browsercore.LoggingCapability
import com.simorgh.feature.browsercore.SimorghWebViewClient

/**
 * Walking skeleton: a single WebView wired to the Capability system.
 * No tabs, no navigation UI yet — see DEVELOPMENT_LOG.md, Phase 1.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val registry = CapabilityRegistry().apply {
            register(LoggingCapability())
        }

        val webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            webViewClient = SimorghWebViewClient(registry)
        }

        setContentView(webView)
        webView.loadUrl("https://example.com")
    }
}
