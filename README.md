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

<p align="center">
  <img src="img/MD.png" alt="JavaFX Markdown Preview Banner" width="150" />
</p>

<h1 align="center">JavaFX Markdown Preview</h1>
<p align="center">
  <strong>⚡ Fast. 🖼️ Beautiful. 🎯 Real-Time. 🌓 Theme-Aware.</strong>
</p>

<p align="center">

  <!-- Maven Central -->
  <a href="https://central.sonatype.com/artifact/io.github.raghul-tech/javafx-markdown-preview">
    <img src="https://img.shields.io/maven-central/v/io.github.raghul-tech/javafx-markdown-preview?style=for-the-badge&color=blueviolet" alt="Maven Central" />
  </a>

  <!-- GitHub Release -->
  <a href="https://github.com/raghul-tech/JavaFX-Markdown-Preview/releases">
    <img src="https://img.shields.io/github/release/raghul-tech/JavaFX-Markdown-Preview.svg?label=Latest%20Release&style=for-the-badge&color=success" alt="GitHub Release" />
  </a>

  <!-- Maven Build Workflow -->
  <a href="https://github.com/raghul-tech/JavaFX-Markdown-Preview/actions/workflows/maven.yml">
    <img src="https://img.shields.io/github/actions/workflow/status/raghul-tech/JavaFX-Markdown-Preview/maven.yml?label=Build&style=for-the-badge&color=brightgreen" alt="Maven Build Status" />
  </a>

  <!-- CodeQL Analysis -->
  <a href="https://github.com/raghul-tech/JavaFX-Markdown-Preview/actions/workflows/codeql.yml">
    <img src="https://img.shields.io/github/actions/workflow/status/raghul-tech/JavaFX-Markdown-Preview/codeql.yml?label=CodeQL&logo=github&style=for-the-badge&color=informational" alt="CodeQL Status" />
  </a>

  <!-- Javadoc -->
  <a href="https://javadoc.io/doc/io.github.raghul-tech/javafx-markdown-preview/1.0.3">
    <img src="https://img.shields.io/badge/Javadoc-1.0.3-blue?style=for-the-badge&logo=java" alt="Javadoc (1.0.3)" />
  </a>

  <!-- Support -->
  <a href="https://buymeacoffee.com/raghultech">
    <img src="https://img.shields.io/badge/Buy%20Me%20a%20Coffee-Support-orange?style=for-the-badge&logo=buy-me-a-coffee" alt="Buy Me A Coffee" />
  </a>
</p>



---

# ☕ About JavaFX Markdown Preview

**JavaFX Markdown Preview** is a modern and lightweight library designed to render Markdown content seamlessly in JavaFX applications.

Whether you’re building a Markdown editor, a documentation viewer, or just need a clean way to preview `.md` content inside your app — this tool saves you hours of work by offering a plug-and-play experience.

### Why use this? 

✅ **Embed anywhere** – Tabs, windows, or any JavaFX layout  
✅ **Live preview** – Automatic updates when files change  
✅ **Syntax highlighting** – Beautiful, GitHub-style code blocks  
✅ **Copy buttons** – One-click copy of code snippets  
✅ **Dark and light themes** – Easy toggling  
✅ **Markdown to HTML export** – Generate styled HTML  
✅ **Cross-platform** – Windows, macOS, Linux  
✅ **No browser dependency** – Uses JavaFX WebView


> Whether you're developing documentation tools, note-taking apps, IDE plugins, or just need a Markdown viewer, this tool offers a ready-made and customizable solution.

---

## ✨ What's New in 1.0.3

>**Note**:
>**Version 1.0.2** contained all the new features below but failed deployment to Maven Central.
>**Version 1.0.3** is the first properly signed and published release.
>Please upgrade to **1.0.3**.

- 🎯 **Tab and TabPane Support**
  - Embed previews inside `TabPane` with lifecycle management.
- 🧩 **Standalone WebView Component**
  - Use a `WebView` node in any JavaFX layout (`VBox`, `HBox`, `BorderPane`).
- 🌈 **Enhanced GitHub-Style Themes**
  - Cleaner dark and light themes matching GitHub styling.
- 📝 **Syntax Highlighting & Copy Buttons**
  - Beautiful code blocks with one-click copy support.
- ⚡ **Improved Real-Time Updates**
  - Live reload when files change, with reduced flicker.
- 💡 **JavaFX 17 Compatibility**
  - Runs smoothly on JavaFX 11–17.

✅ **Why use 1.0.3?** 
>This version ensures a verified signature, successful publication to Maven Central, and fully reproducible builds.

---

## 📦 Available Modules

| Artifact Name                       | Includes Flexmark | Use Case                         |
|------------------------------------|-------------------|-----------------------------------|
| `javafx-markdown-preview`         | ❌ No              | For Maven users with their own Flexmark setup    |
| `javafx-markdown-preview-all`     | ✅ Yes            | Works out of the box, no extra setup       |

---

## ✨ Features

- 📂 Load `.md` file or raw Markdown string
- 🔄 Real-time preview with file change detection
- 🖥️ Opens in a separate JavaFX window
- 🔁 Reopen preview window if closed
- 🧩 Embed in any JavaFX scene or layout
- ☕ Supports both Java and JavaFX projects
- ⚡ Lightweight and reusable as a utility

---

## 📦 Install via Maven

### 🛠️ Option 1: All-in-One (Flexmark + Code)

Add the dependency in your `pom.xml`:

```xml
<dependency>

  <groupId>io.github.raghul-tech</groupId>
  
  <artifactId>javafx-markdown-preview-all</artifactId>
  
  <version>1.0.3</version>
  
</dependency>
```
Or browse it on [Maven Central](https://central.sonatype.com/artifact/io.github.raghul-tech/javafx-markdown-preview-all).

### 🪶 Option 2: Minimal (bring your own Flexmark)

```xml
<dependency>
  <groupId>io.github.raghul-tech</groupId>
  <artifactId>javafx-markdown-preview</artifactId>
  <version>1.0.3</version>
</dependency>

<dependency>
  <groupId>com.vladsch.flexmark</groupId>
  <artifactId>flexmark-all</artifactId>
  <version>0.64.8</version>
</dependency>
```

Or browse it on [Maven Central](https://central.sonatype.com/artifact/io.github.raghul-tech/javafx-markdown-preview).

---

## 🏗️ Example Projects
- You’ll find ready-to-run examples in the [`examples/`](examples/) directory:

	- [`ExampleWindow.java`](examples/ExampleWindow.java) – Launches the preview in a **standalone JavaFX window**, ideal for external Markdown viewers or editor popouts.

	- [`ExampleTabPane.java`](examples/ExampleTabPane.java) – Embeds the preview as a tab inside a `TabPane`, great for multi-tab document editors.

	- [`ExampleWebview.java`](examples/ExampleWebview.java) –  Shows how to **embed the preview WebView** into any JavaFX layout, such as `VBox`, `BorderPane`, or `StackPane`.
	
	- [`ExampleRenderer.java`](examples/ExampleRenderer.java) –  Demonstrates using the `MarkdownRenderer` utility to **convert Markdown content into styled HTML**, useful for exporting or rendering elsewhere. 

✅ To run an example:

1. Download or clone this repository.

2. Navigate to [`examples/`](examples/) folder.

3. Compile and run the desired file.

> ✅ Tip: Each example is standalone—feel free to adapt and copy the patterns directly into your project.

---

## 💡 Run Using JAR
### **Compile:**

```bash
javac -cp java-markdown-preview-all-1.0.3.jar Markdown.java
```
### **Run:**

> Windows:
```bash
java -cp .;java-markdown-preview-all-1.0.3.jar Markdown
```

> Linux/macOS:
```bash
java -cp .:java-markdown-preview-all-1.0.3.jar Markdown
```
---



## 🔍 Documentation

- 📚 [Javadoc](https://javadoc.io/doc/io.github.raghul-tech/javafx-markdown-preview)

- 📝 [Changelog](CHANGELOG.md)

- ❓ [Issue Tracker](https://github.com/raghul-tech/javafx-Markdown-Preview/issues)

---


## 🧩 Requirements
- OpenJFX libraries if using Java 11+

- JavaFX (use OpenJFX for Java 11+)

- Maven for dependency management

---

## 🆕 Changelog:

* View all releases on the [Releases Page.](https://github.com/raghul-tech/JavaFX-Markdown-Preview/releases)
* For a detailed log of all changes, refer to the [CHANGELOG.md](CHANGELOG.md) file.

---

## 🤝 Contributing
* We welcome contributions of all kinds:

   * 🛠️ Bug fixes

   * 🌟 Feature suggestions

   * 📚 Documentation improvements

   * 🧪 More usage examples

> Please check the [Contributing Guide](CONTRIBUTING.md) before starting.

---

## 🐞 Report a Bug
   * If you've encountered a bug, please report it by clicking the link below. 
   	This will guide you through the bug-reporting process:
   	➡️ [Click here to report a bug](https://github.com/raghul-tech/JavaFX-Markdown-Preview/issues/new?template=bug_report.yaml)
 
---

## 📄 License
- This project is licensed under the [GNU General Public License v3 (GPL-3.0)](LICENSE).

---

## 📬 Contact
Email: [raghultech.app@gmail.com](mailto:raghultech.app@gmail.com)

---

## ☕ Support
> If you find this project useful, consider buying me a coffee!

<a href="https://buymeacoffee.com/raghultech"> <img src="https://img.shields.io/badge/Buy%20Me%20A%20Coffee-Support-orange.svg?style=flat-square" alt="Buy Me A Coffee" /> </a> 


