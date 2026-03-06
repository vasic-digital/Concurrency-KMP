/*#######################################################
 *
 * SPDX-FileCopyrightText: 2025 Milos Vasic
 * SPDX-License-Identifier: Apache-2.0
 *
 * Lazy Loading Utilities
 * Memory-efficient loading for large documents
 * Thread-safe via Mutex for Kotlin Multiplatform compatibility
 *
 *########################################################*/
package digital.vasic.concurrency

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Lazy Document Loader.
 *
 * Loads large documents in chunks to avoid memory issues. All mutable state
 * is protected by a [Mutex] to prevent race conditions during concurrent access.
 *
 * @param T The type of document content
 * @param chunkSize Number of lines to load per chunk
 */
open class LazyDocumentLoader<T>(
    protected val chunkSize: Int = 1000
) {
    private val mutex = Mutex()
    private val _chunks = mutableMapOf<Int, T?>()
    private var totalChunks: Int = 0
    private val loadingChunks = mutableSetOf<Int>()

    fun initialize(totalSize: Int) {
        totalChunks = (totalSize + chunkSize - 1) / chunkSize
    }

    suspend fun getChunk(index: Int): T? {
        mutex.withLock {
            if (index !in 0 until totalChunks) return null
            _chunks[index]?.let { return it }
        }

        while (true) {
            val shouldLoad = mutex.withLock {
                _chunks[index]?.let { return it }

                if (index in loadingChunks) {
                    false
                } else {
                    loadingChunks.add(index)
                    true
                }
            }

            if (shouldLoad) {
                try {
                    val chunk = loadChunk(index)
                    mutex.withLock {
                        _chunks[index] = chunk
                    }
                    return chunk
                } finally {
                    mutex.withLock {
                        loadingChunks.remove(index)
                    }
                }
            } else {
                delay(10)
            }
        }
    }

    protected open suspend fun loadChunk(index: Int): T? = null

    suspend fun preloadAround(index: Int, range: Int = 2) {
        val maxIndex = mutex.withLock { totalChunks - 1 }
        coroutineScope {
            (maxOf(0, index - range)..minOf(maxIndex, index + range)).forEach { i ->
                launch { getChunk(i) }
            }
        }
    }

    suspend fun clear() {
        mutex.withLock {
            _chunks.clear()
        }
    }

    suspend fun getMemoryUsage(): Long {
        return mutex.withLock {
            _chunks.size.toLong() * chunkSize.toLong()
        }
    }
}

/**
 * Lazy String Loader for text documents.
 *
 * Splits content into line-based chunks and loads them on demand.
 */
class LazyStringLoader(
    private val content: String,
    chunkSize: Int = 1000
) : LazyDocumentLoader<String>(chunkSize) {

    init {
        initialize(content.lines().size)
    }

    override suspend fun loadChunk(index: Int): String? {
        val lines = content.lines()
        val start = index * chunkSize
        val end = minOf(start + chunkSize, lines.size)

        if (start >= lines.size) return null

        return lines.subList(start, end).joinToString("\n")
    }

    suspend fun getLines(startLine: Int, endLine: Int): List<String> {
        val result = mutableListOf<String>()
        var current = startLine / chunkSize

        while (current * chunkSize < endLine) {
            val chunk = getChunk(current) ?: break
            result.addAll(chunk.lines())
            current++
        }

        return result.drop(startLine % chunkSize).take(endLine - startLine)
    }
}

/**
 * Flow-based lazy loader.
 *
 * Provides a [StateFlow] of loaded content that UI can collect.
 */
class FlowLazyLoader<T>(
    private val chunkSize: Int = 1000
) {
    private val mutex = Mutex()
    private val _content = MutableStateFlow<List<T>>(emptyList())
    val content: StateFlow<List<T>> = _content.asStateFlow()

    private var loadedChunks = 0
    private val parentJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + parentJob)

    suspend fun loadMore(items: List<T>) {
        mutex.withLock {
            val current = _content.value.toMutableList()
            current.addAll(items)
            _content.value = current
            loadedChunks++
        }
    }

    fun getVisibleRange(visibleStart: Int, visibleEnd: Int, buffer: Int = 10): IntRange {
        val size = _content.value.size
        val start = maxOf(0, visibleStart - buffer)
        val end = minOf(size, visibleEnd + buffer)
        return start until end
    }

    fun cleanup() {
        parentJob.cancel()
        _content.value = emptyList()
    }
}
