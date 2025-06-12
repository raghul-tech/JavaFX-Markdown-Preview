# 📦 Changelog

All notable changes to **JavaFX Markdown Preview** will be documented in this file.

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
