# ADR-0002: Browser Capability System

## Status

Accepted

## Context

The browser needs to support future capabilities such as:

- AI summarization
- Reader mode
- Persian language rendering
- Content extraction
- Ad blocking
- Browser automation

Implementing these features directly inside the browser core would make the system difficult to maintain and extend.

## Decision

We will use a Capability-based architecture.

The browser core will expose a stable interface:

```kotlin
interface BrowserCapability {
    val id: String
    fun onPageStarted(context: PageContext) {}
    fun onPageFinished(context: PageContext) {}
    fun onContentExtracted(content: PageContent) {}
}
New capabilities will be added as independent modules that connect to the browser through this interface.

Examples:

AiSummaryCapability
PersianRenderingCapability
ReaderModeCapability
MemoryCaptureCapability
Consequences

Positive:

New features can be added without modifying browser core.
Different teams can develop capabilities independently.
Easier testing and maintenance.

Trade-offs:

More abstraction layers.
Requires clear interface design.
