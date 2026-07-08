# ADR-0004: On-device Memory Strategy

## Status

Accepted

## Context

Simorgh AI Research Browser needs memory capabilities to provide a personalized research experience.

The browser may need to remember:

- Previously visited pages
- User research context
- Important documents
- Related knowledge connections
- User preferences

A memory system is required for AI agents and intelligent browsing features.

The first version should prioritize:

- User privacy
- Offline capability
- Low infrastructure cost
- Data ownership

## Decision

The initial memory system will use an on-device approach.

Memory components:

| Memory Type | Technology | Purpose |
|---|---|---|
| Episodic Memory | Room Database | Store browsing events and user interactions |
| Vector Memory | sqlite-vec | Semantic search over saved content |
| Knowledge Graph | Room relational tables | Store entities and relationships |

The memory layer will be accessed through a dedicated interface:

```kotlin
interface MemoryManager {
    suspend fun recordVisit(page: PageContent)
    suspend fun recallSimilar(query: String): List<MemoryChunk>
    suspend fun getUserProfile(): UserKnowledgeProfile
}
Cloud synchronization may be added in future versions as an optional feature.

Consequences

Positive:

Better privacy because data stays on the device.
Works with limited or no network connection.
Reduces dependency on external infrastructure.
Gives users ownership of their data.

Trade-offs:

Local storage limitations.
More complexity in device resource management.
Advanced synchronization requires future development.
