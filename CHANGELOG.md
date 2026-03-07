<!-- SPDX-FileCopyrightText: 2025 Milos Vasic -->
<!-- SPDX-License-Identifier: Apache-2.0 -->

# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-03-06

### Added
- Initial release extracted from Yole monolith
- `LazyDocumentLoader<T>` - Generic chunk-based lazy loading with configurable chunk size and preloading
- `LazyStringLoader` - Text document lazy loading with line-based chunks
- `FlowLazyLoader<T>` - StateFlow-based lazy content loading for reactive UI integration
- `platformSynchronized` - Multiplatform synchronized block via expect/actual declarations
- Kotlin Multiplatform support (Android, Desktop/JVM, iOS, Wasm/JS)
- Comprehensive test suite
- CI/CD via GitHub Actions

### Infrastructure
- Gradle build with version catalog
- Kover code coverage
- SPDX license headers (Apache-2.0)
