# Concurrency-KMP API Reference

## Package: `digital.vasic.concurrency`

### class LazyDocumentLoader<T>

| Method | Return | Description |
|--------|--------|-------------|
| `initialize(totalSize)` | Unit | Set total content size |
| `suspend getChunk(index)` | T? | Get chunk by index |
| `suspend preloadAround(index, range)` | Unit | Preload adjacent chunks |
| `suspend clear()` | Unit | Free loaded chunks |
| `suspend getMemoryUsage()` | Long | Estimated memory usage |

### class LazyStringLoader

Extends `LazyDocumentLoader<String>`.

| Method | Return | Description |
|--------|--------|-------------|
| `suspend getLines(startLine, endLine)` | List<String> | Get range of lines |

### class FlowLazyLoader<T>

| Property | Type | Description |
|----------|------|-------------|
| `content` | StateFlow<List<T>> | Observable loaded content |

| Method | Return | Description |
|--------|--------|-------------|
| `suspend loadMore(items)` | Unit | Add more items |
| `getVisibleRange(start, end, buffer)` | IntRange | Compute visible range with buffer |
| `cleanup()` | Unit | Release resources |

### fun platformSynchronized

```kotlin
expect inline fun <R> platformSynchronized(lock: Any, block: () -> R): R
```
