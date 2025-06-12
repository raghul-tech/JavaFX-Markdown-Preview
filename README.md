<p align="center">
  <img src="img/MD.png" alt="JavaFX Markdown Preview Banner" width="150" />
</p>

<h1 align="center">JavaFX Markdown Preview</h1>

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
  <a href="https://javadoc.io/doc/io.github.raghul-tech/javafx-markdown-preview/1.0.1">
    <img src="https://img.shields.io/badge/Javadoc-1.0.1-blue?style=for-the-badge&logo=java" alt="Javadoc (1.0.1)" />
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

✅ **Plug-and-play setup** – Integrate in just minutes  
✅ **Live preview support** – Automatically updates when the file changes  
✅ **Cross-platform UI** – Works on Windows, macOS, and Linux  
✅ **Flexmark-powered** – Renders CommonMark-compliant Markdown  
✅ **No browser dependencies** – Uses JavaFX WebView internally  
✅ **Reusable** – Add to any JavaFX or plain Java project easily

> Whether you're developing documentation tools, note-taking apps, IDE plugins, or just need a Markdown viewer, this tool offers a ready-made and customizable solution.

---

## ✨ Features

- 📂 Load `.md` file or raw Markdown string
- 🔄 Real-time preview with file change detection
- 🖥️ Opens in a separate JavaFX window
- 🔁 Reopen preview window if closed
- ☕ Supports both Java and JavaFX projects
- ⚡ Lightweight and reusable as a utility

---

## 📦 Install via Maven

Add the dependency in your `pom.xml`:

```xml
<dependency>

  <groupId>io.github.raghul-tech</groupId>
  
  <artifactId>javafx-markdown-preview</artifactId>
  
  <version>1.0.1</version>
  
</dependency>
```
Or browse it on [Maven Central](https://central.sonatype.com/artifact/io.github.raghul-tech/javafx-markdown-preview).

---

## 🚀 Usage Example
1. Load from Markdown File

```java
import com.raghultech.markdown.preview.JavaFXMarkdownPreview;
import java.io.File;
import java.util.Scanner;

public class Markdown {
    public static void main(String[] args) {
        File file = new File("demo.md");
        JavaFXMarkdownPreview preview = new JavaFXMarkdownPreview(file);
        preview.launchPreview();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            while (preview.isPreviewShowing()) {
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }

            System.out.print("Preview window closed. Reopen? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                preview.reopenWindow();
            } else {
                System.out.println("Exiting...");
                break;
            }
        }

        scanner.close();
        System.exit(0);
    }
}
```
2. Load from Markdown String

```java
import com.raghultech.markdown.preview.JavaFXMarkdownPreview;
import java.util.Scanner;

public class Markdown {
    public static void main(String[] args) {
        String content = "# ☕ JavaFX Markdown Preview\n\n" +
            "A simple and elegant **Markdown preview tool** using JavaFX and Flexmark.\n\n" +
            "---\n\n" +
            "## ✨ Features\n" +
            "- 📂 Load `.md` files or string input\n" +
            "- 🔄 Real-time file change detection\n" +
            "- 🖥️ Separate JavaFX window for previewing\n" +
            "- 🔁 Reopen preview window if closed\n";

        JavaFXMarkdownPreview preview = new JavaFXMarkdownPreview(content);
        preview.launchPreview();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            while (preview.isPreviewShowing()) {
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }

            System.out.print("Preview window closed. Reopen? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                preview.reopenWindow();
            } else {
                System.out.println("Exiting...");
                break;
            }
        }

        scanner.close();
        System.exit(0);
    }
}
```

---

## 💡 Run Using JAR
### **Compile:**

```bash
javac -cp JavaFX-Markdown-Preview-1.0.1.jar Markdown.java
```
### **Run:**

> Windows:
```bash
java -cp .;JavaFX-Markdown-Preview-1.0.1.jar Markdown
```

> Linux/macOS:
```bash
java -cp .:JavaFX-Markdown-Preview-1.0.1.jar Markdown
```
---

## 🧩 Requirements
- Java 8 or above

- JavaFX (use OpenJFX for Java 11+)

- Maven for dependency management

---

## 🆕 Changelog:

* View all releases on the [Releases Page.](https://github.com/raghul-tech/JavaFX-Markdown-Preview/releases)
* For a detailed log of all changes, refer to the [CHANGELOG.md](CHANGELOG.md) file.

---

## ⚠️ Known Issues
#### 🔄 Real-Time Typing Issues
- The preview may break briefly when typing fast due to background re-parsing.

- Workaround: Pause briefly or trigger a manual refresh if needed.

#### 😀 Emoji Rendering in Offline Environments
- Emoji rendering depends on online font support (e.g., via web or CDN).

- Offline systems may display missing glyphs or boxes.

- This is a system font limitation, not a library bug.

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
   	➡️ [Click here to report a bug](https://github.com/raghul-tech/JavaFX-Markdown-Preview/issues)
 
---

## 📄 License
- This project is licensed under the [MIT License](LICENSE).

---

## 📬 Contact
Email: [raghultech.app@gmail.com](mailto:raghultech.app@gmail.com)

---

## ☕ Support
> If you find this project useful, consider buying me a coffee!

<a href="https://buymeacoffee.com/raghultech"> <img src="https://img.shields.io/badge/Buy%20Me%20A%20Coffee-Support-orange.svg?style=flat-square" alt="Buy Me A Coffee" /> </a> 


