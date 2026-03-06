# Concurrency-KMP

Kotlin Multiplatform concurrency utilities: lazy document loading, platform synchronization, and flow-based lazy loaders.

## Features

- **LazyDocumentLoader** - Generic chunk-based lazy loading with preloading
- **LazyStringLoader** - Text document lazy loading with line-based chunks
- **FlowLazyLoader** - StateFlow-based lazy content loading for UI
- **platformSynchronized** - Multiplatform synchronized block (expect/actual)

## Platforms

| Platform | Status |
|----------|--------|
| Android | Supported |
| Desktop (JVM) | Supported |
| iOS | Supported |
| Web (Wasm) | Supported |

## Quick Start

```kotlin
import digital.vasic.concurrency.LazyStringLoader

val loader = LazyStringLoader(largeContent, chunkSize = 500)
val firstChunk = loader.getChunk(0)
loader.preloadAround(2, range = 1)
```

## License

Apache-2.0
