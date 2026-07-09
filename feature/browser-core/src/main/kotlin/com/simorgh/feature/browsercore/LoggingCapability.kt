package com.simorgh.feature.browsercore

import android.util.Log

/**
 * Test capability used only to verify the Capability system works
 * end-to-end (Phase 1). Not a real feature — safe to delete once a
 * real capability (e.g. MemoryCaptureCapability) exists.
 */
class LoggingCapability : BrowserCapability {

    override val id: String = "logging-capability"

    override fun onPageStarted(context: PageContext) {
        Log.d("CapabilitySystem", "onPageStarted: ${context.url}")
    }

    override fun onPageFinished(context: PageContext) {
        Log.d("CapabilitySystem", "onPageFinished: ${context.url}")
    }
}
