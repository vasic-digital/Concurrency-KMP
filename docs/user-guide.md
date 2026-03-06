# Concurrency-KMP User Guide

## LazyStringLoader

```kotlin
val loader = LazyStringLoader(content, chunkSize = 1000)
val chunk = loader.getChunk(0)        // Load first chunk
val lines = loader.getLines(0, 50)    // Get lines 0-49
loader.preloadAround(5, range = 2)    // Preload chunks 3-7
loader.clear()                        // Free memory
```

## FlowLazyLoader

```kotlin
val loader = FlowLazyLoader<String>()
loader.loadMore(listOf("a", "b", "c"))

// In UI
loader.content.collect { items -> display(items) }

// IMPORTANT: cleanup when done
loader.cleanup()
```

## platformSynchronized

```kotlin
val lock = Any()
val result = platformSynchronized(lock) {
    // thread-safe block
    computeValue()
}
```
