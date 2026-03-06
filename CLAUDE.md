# CLAUDE.md - Concurrency-KMP

## Overview

`digital.vasic.concurrency` is a Kotlin Multiplatform library providing concurrency utilities: lazy document loading, platform synchronization, and flow-based lazy loaders.

**Module**: Concurrency-KMP (KMP, 4 targets: Android, Desktop/JVM, iOS, Wasm)
**Package**: `digital.vasic.concurrency`

## Build & Test

```bash
./gradlew build
./gradlew desktopTest
./gradlew compileKotlinDesktop
```

## Architecture

| Class | Purpose |
|-------|---------|
| `LazyDocumentLoader<T>` | Generic chunk-based lazy loading with mutex protection |
| `LazyStringLoader` | Line-based text document lazy loading |
| `FlowLazyLoader<T>` | StateFlow-based lazy loader for UI collection |
| `platformSynchronized` | expect/actual synchronized block |

## expect/actual

`platformSynchronized` has actual implementations on all 4 platforms:
- Android/Desktop: delegates to `synchronized(lock, block)`
- iOS/Wasm: executes block directly (single-threaded)

## Dependencies

- `kotlinx-coroutines-core` (Mutex, StateFlow, CoroutineScope)

## Commit Style

Conventional Commits: `feat(concurrency): add lazy batch loader`
