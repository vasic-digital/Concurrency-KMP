# Concurrency-KMP Architecture

## Overview

Provides concurrency primitives for Kotlin Multiplatform: lazy loading with chunk-based memory management, platform-abstracted synchronization, and flow-based observable loaders.

## Components

- `LazyDocumentLoader<T>` - Abstract base with mutex-protected chunk caching
- `LazyStringLoader` - Concrete loader splitting text into line-based chunks
- `FlowLazyLoader<T>` - StateFlow wrapper for progressive content loading
- `platformSynchronized` - expect/actual for cross-platform synchronized blocks

## Dependencies

- `kotlinx-coroutines-core`: Mutex, StateFlow, CoroutineScope, SupervisorJob

## Platform Implementations

`platformSynchronized`:
- JVM (Android/Desktop): `synchronized(lock, block)`
- Native (iOS): `block()` (single-threaded execution model)
- Wasm/JS: `block()` (single-threaded)
