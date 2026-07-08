# Contributing to Simorgh AI Research Browser

Thank you for your interest in contributing to Simorgh AI Research Browser.

Simorgh AI Research Browser is an open-source AI-powered research browser focused on AI-assisted browsing, Persian language intelligence, Claude integration, and an extensible architecture.

We welcome contributions from developers, researchers, designers, and AI enthusiasts.

---

## Before You Start

Before contributing, please read:

- [ARCHITECTURE.md](https://github.com/SimorghAI-Community/simorgh-ai-browser/blob/main/ARCHITECTURE.md)
- [docs/roadmap.md](https://github.com/SimorghAI-Community/simorgh-ai-browser/blob/main/docs/roadmap.md) — to see which phase the project is currently in

Understand the project's architecture principles:

- Modular by Feature
- Core stability
- Interface-first design

For major technical changes, open a discussion before implementation.

---

## Development Principles

### 1. Modular by Feature

Each major capability should be implemented as an independent module.
Adding a new feature should not require modifying existing modules unnecessarily.

Good:
feature/
├── ai-agent/
└── persian-intelligence/

Bad:
browser-core/
└── aiFeature.kt

AI features, memory systems, language processing, and other capabilities should not be directly implemented inside the browser core.

### 2. Interface First

Modules should communicate through interfaces instead of direct implementations.

Important interfaces include:

- `AiEngine`
- `BrowserCapability`
- `MemoryManager`
- `AgentTool`

This allows replacing components without breaking the rest of the system.

Examples:

- Changing AI providers
- Adding local AI models
- Replacing database implementations
- Adding new browser capabilities

### 3. Keep Core Small

The browser core should only handle essential browser responsibilities:

- WebView management
- Navigation
- Tabs
- Capability system

The following should remain separate modules:

- AI features
- Agent system
- Memory
- Persian intelligence

---

## Development Environment

Required:

- Android Studio (latest stable version)
- Kotlin
- JDK 17
- Gradle

Clone the repository:

```bash
git clone https://github.com/SimorghAI-Community/simorgh-ai-browser.git
```

Open the project in Android Studio and wait for Gradle synchronization.

---

## Branch Naming

Use clear branch names.

Feature: `feature/add-ai-summary`

Bug fix: `fix/webview-crash`

Documentation: `docs/update-architecture`

---

## Commit Convention

Use Conventional Commits.

Examples:
feat: add AI summary capability
fix: handle WebView crash
docs: update architecture documentation
test: add AiEngine tests

---

## Adding New Dependencies

Before adding a new external dependency:

1. Explain why it is needed.
2. Consider possible alternatives.
3. Add an Architecture Decision Record (ADR) if the decision affects architecture.

ADR files are located at:
docs/adr/

A template is available at `docs/adr/template.md`.

---

## Pull Request Guidelines

Every Pull Request should include:

- Clear description of the change
- Reason for the change
- Testing information
- Screenshots for UI changes

Before submitting:

- Build successfully
- Run tests
- Check formatting

---

## Adding New Features

For a new browser capability:

1. Create the required module.
2. Define the required interface.
3. Implement the feature.
4. Add tests.
5. Update documentation if needed.

---

## Code Quality

Please:

- Keep code simple and readable.
- Write tests for important logic.
- Avoid unnecessary dependencies.
- Document complex decisions.

---

## Community Guidelines

Please keep discussions respectful and constructive.

Different technical opinions are welcome. The goal is building a reliable, open, and extensible AI browser together.

Thank you for contributing to Simorgh AI Research Browser.
