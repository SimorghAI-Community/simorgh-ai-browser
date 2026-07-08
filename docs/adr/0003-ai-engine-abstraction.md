# ADR-0003: AI Engine Abstraction

## Status

Accepted

## Context

Simorgh AI Research Browser will use artificial intelligence capabilities for:

- Page summarization
- Chat with web content
- Translation
- Research assistance
- AI agent workflows

AI providers and models will change over time. The architecture should not depend directly on a single provider.

A direct connection between features and a specific AI API would make future changes difficult.

## Decision

All AI interactions must go through an abstraction layer called `AiEngine`.

The core interface:

```kotlin
interface AiEngine {
    suspend fun summarize(text: String): Result<String>
    suspend fun chat(messages: List<AiMessage>): Flow<AiStreamChunk>
    suspend fun translate(text: String): Result<String>
}
AI providers will implement this interface.

Examples:

ClaudeAiEngine
OpenAiEngine
LocalAiEngine
HybridAiEngine

Feature modules must never call external AI APIs directly.

Consequences

Positive:

AI providers can be replaced easily.
Local and cloud models can coexist.
Features remain independent from AI vendors.
Easier testing using mock AI engines.

Trade-offs:

Additional abstraction layer.
More initial architecture work.
