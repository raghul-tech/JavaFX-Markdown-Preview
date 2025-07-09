<!--
 * Copyright (C) 2025 Raghul-tech
 * https://github.com/raghul-tech
 * This file is part of JavaFX Markdown Preview.
 *
 * JavaFX Markdown Preview is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JavaFX Markdown Preview is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JavaFX Markdown Preview.  If not, see <https://www.gnu.org/licenses/>.
-->

# 📦 Changelog

All notable changes to **JavaFX Markdown Preview** will be documented in this file.

---

## [1.0.2] - 2025-07-09

### ✨ Added
- ✅ **Tab and TabPane Integration**
  - Simplified APIs to embed previews directly inside `TabPane`.
  - Improved `MarkdownTab` to support `relaunchTab()` and enhanced lifecycle management.
- ✅ **Standalone WebView Component**
  - Introduced a new class that returns a `WebView` node, allowing you to embed previews **anywhere** in your JavaFX layouts (e.g., `VBox`, `HBox`, `BorderPane`).
- ✅ **Syntax Highlighting**
  - Code blocks now automatically use syntax highlighting.
- ✅ **Copy Button**
  - Added a convenient copy button for all code blocks in the preview.
- ✅ **Improved Dark and Light Themes**
  - Visual styles now more closely match GitHub Markdown rendering.
- ✅ **Enhanced Real-Time Updates**
  - File changes are instantly reflected without flicker.
- ✅ **MarkdownRenderer Enhancements**
  - Added ability to get **styled HTML** for exporting or embedding.
- ✅ **Example Projects**
  - Added multiple example apps demonstrating:
    - Scene embedding
    - Stage embedding
    - VBox embedding
    - TabPane integration
    - Custom WebView integration

### 🛠️ Improved
- 🪄 **Window Launch Logic**
  - More reliable lifecycle—no crashes when reopening windows.
- ⚡ **Performance**
  - Faster initial rendering and reduced memory usage with large Markdown files.
- 🧹 **Refactoring**
  - Clear separation between:
    - **`javafx-markdown-preview`** (minimal core build)
    - **`javafx-markdown-preview-all`** (with Flexmark and full source)
- 🪟 **Window Customization**
  - Easier configuration of window title, size, and icons.
- 🎯 **Compatibility**
  - Switched from JavaFX 23 to **JavaFX 17** for better support across older Java versions (Java 11+).

### 🧩 Artifacts
- 🏷️ **`javafx-markdown-preview`**
  - Minimal build with only essential classes.
- 🏷️ **`javafx-markdown-preview-all`**
  - Bundled version including Flexmark and example sources.

### 🐞 Fixed
- 🐛 Resolved dark/light theme switching issues.
- 🐛 Fixed occasional `NullPointerException` when re-adding `MarkdownTab`.
- 🐛 Corrected encoding problems in previews with special characters.

---

## ⚖️ License Change
- 📝 **License updated**:  
  - **From:** MIT  
  - **To:** GNU General Public License v3 (GPL-3.0)

  > This ensures contributions remain open-source while allowing broader usage.

---

## 💡 Tip
- You can now use the **WebView-based preview in any JavaFX Node** (e.g., `VBox`, `StackPane`, `BorderPane`) for maximum flexibility.

---

## [1.0.1] - 2025-06-12

### ✨ Added
- ✅ **Dual input support**: Preview from a `.md` **file** or **raw string content**.
- 🪟 **Reopen preview window** after closing — useful for interactive demos or CLI tools.
- 🖼️ **Screenshot and project badges** added to README for better presentation.
- 📁 **Example usage code** added under the `examples/` folder.
- 📤 **Maven Central instructions** and manual `.jar` usage guide added.
- 📜 **CONTRIBUTING.md** and GitHub **issue templates** added for community support.

### 🛠️ Improved
- 🧹 Refactored `JavaFXMarkdownPreview` class to support both `File` and `String` inputs.
- 🪄 Optimized JavaFX thread handling and WebView initialization logic.
- 📘 Enhanced documentation with clearer setup steps and inline code blocks.

### 🐞 Fixed
- 🐛 Fixed preview not updating correctly when launched with string content.
- 🐛 Resolved encoding issues when previewing Markdown files with special characters.
- ⚠️ Prevented crashes on empty or invalid Markdown files.

---

## ⚠️ Known Issues

### 🔄 Real-Time Typing Issues
- The Markdown preview may occasionally break or flicker while typing quickly.
- This happens because the renderer tries to parse the content mid-edit.
- **Workaround:** Pause briefly between edits or trigger a manual refresh if needed.

### 😀 Emoji Rendering in Offline Environments
- Emojis may not render correctly when offline — especially if loaded via CDNs or external sources.
- Some emojis may appear as empty boxes or missing symbols.
- ➕ This is due to font or system limitations, not a bug in the library.

> If you encounter other issues, feel free to [open an issue](https://github.com/raghul-tech/javafx-markdown-preview/issues).

---

## [1.0.0] - 2025-06-12

- 🎉 Initial release of **JavaFX Markdown Preview**!
- Preview `.md` files using JavaFX WebView and Flexmark library.
- Minimal, embeddable, and designed for easy Markdown rendering in Java apps.
