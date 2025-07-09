# 📝 JavaFX Markdown Preview
- A modern Java library for **live Markdown rendering in JavaFX**.

	- ✅ Real-time rendering of Markdown content

	- ✅ Theme switching (light/dark)

	- ✅ Supports loading from `File` and `String`

	- ✅ Standalone windows, tabs, or embeddable WebViews

	- ✅ Export to styled HTML

---

# 📦 Key Components
Below is an overview of each class and how to use them.
---

## 🎯 `MarkdownWebView` (Embeddable WebView)
> ✅ **Use this when you want a WebView to embed inside a Scene or layout.**

### ✨ How to Use

```java
// Create from File
MarkdownWebView preview = new MarkdownWebView(new File("README.md"), getHostServices());

// Or from String
MarkdownWebView preview = new MarkdownWebView("# Hello Markdown", getHostServices());

// Launch as a Node (WebView) to add to Scene
Scene scene = new Scene(preview.launch(), 800, 600);
```

### ✅ Tip: You can also pass an existing WebView for full control.

---

## 🎛 Features
| Method				|	Purpose
|------------------------|-------------------------------------------
| `launch()`             |    To launch the preview it will return a webview 
| `setCurrentFile(File)`	| 	Load a Markdown file				
| `setContent(String)`	|	Load Markdown from a String
| `setDarkMode(boolean)`	|	Toggle dark/light theme
| `dispose()`			|	Release resources
| `isPreviewShowing()`	|	Check if the preview is visible
| `isDisposed()`		|	Check if resources have been disposed
| `getWebView()`		|	Get the underlying WebView
|` updatefullContent()`	|    To refesh the entire preview


### ✅ Real-time file monitoring
- When initialized with a `File`, changes on disk will automatically update the preview when you save.

---

## 🧪 WebView Examples
- Below are example classes that demonstrate different ways to embed `MarkdownWebView`:

### `ExampleScenePreview`
- Embed a preview in a standalone Scene.

### `ExampleStagePreview`
- Embed in a Stage window.

### `ExampleTabPreview`
- Embed in a Tab.

### `ExampleVBoxPreview`
- Embed inside a VBox layout.

### `ExampleWebView`
- Embed in a layout alongside buttons to dispose dynamically.

### Additional Examples
> Other examples show combining WebView with custom controls, tabs, or toolbars.
- ✅ Usage pattern (common to all):

```java
MarkdownWebView preview = new MarkdownWebView(fileOrString, getHostServices());
preview.setDarkMode(true);
preview.launch(); // Returns WebView Node
```
---

## 🎯 `MarkdownTab` (Tabbed Interface)
> ✅ **Use this when you want a Markdown preview inside a TabPane.**

### ✨ How to Use

```java
TabPane tabPane = new TabPane();
Tab tab = new Tab("Preview");
MarkdownTab preview = new MarkdownTab(tab, new File("README.md"));
preview.setHostServices(getHostServices());
preview.setDarkMode(true);
preview.launchTab();
```

### ✅ Tip:
- After disposal, do not call `launchTab()` again without re-creating the instance.
- Use `relaunchTab()` 

---

## 🎛 Features
|Method				 |	Purpose
|-------------------------|----------------------------------
|`setCurrentFile(File)`	 |   Load Markdown from a file
|`setContent(String)`		 |	Load Markdown from a String
|`setDarkMode(boolean)`	 |	Toggle theme
|`setHostServices(HostServices)	`      |	Set link handling
|`dispose()`			 |	Clean up resources
|`isPreviewShowing()`		 |	Check if preview is visible
|`setTabName(String)`		 |	Customize the tab title
|`setTabIcon(String)`		 |	Set a custom icon in the tab
|`setCloseButton(button)`	 |	To add own button and logic 
| `updatefullContent()`	 |    To refesh the entire preview
|`launchTab()`			 |	To launch the tab 
|`relaunchTab()`		 |  To reopen the tab 


### ✅ Example Use Case:
- Combine editor tabs with live preview tabs in one `TabPane`.

--- 

## 🎯 `MarkdownWindow` (Standalone Viewer)
> ✅ **Use this when you want an independent preview window.**

### ✨ How to Use
```java
MarkdownWindow preview = new MarkdownWindow(new File("README.md"));
preview.setWindowName("Markdown Preview");
preview.setWindowSize(900, 700);
preview.setDarkMode(true);
preview.launchPreview();
```

###   ✅ Tip: You can also create it from String content.

---

## 🎛 Features
|Method				  |  	Purpose
|--------------------------|-----------------------
|`setCurrentFile(File)`   	  |	Load Markdown from file
|`setContent(String)`	  |   Load Markdown from String
|`setWindowName(String)`	  |	Set window title
|`setWindowSize(int, int)`	  |	Set window dimensions
|`setIcon(String)`		  |	Set window icon
|`setDarkMode(boolean)`	  |	Toggle dark/light theme
|`dispose()`			  |	Clean up resources
|`isPreviewShowing()`		  |	Check if the window is visible
|`reopenWindow()`		  |	Reopen after closing
| `updatefullContent()`	  |    To refesh the entire preview  
|`launchTab()`			 |   TO open the window

---

### Examples includes:

- `ExampleWindow`
	- Opens a preview in a background thread with user prompt to reopen.

- `ExampleWindow2`
	- Opens a preview using JavaFX `Application`.

---

## 🧪 `MarkdownRenderer` (Render HTML)
> ✅ **Use this utility if you only need rendered HTML without displaying it.**

### ✨ How to Use

```java
MarkdownRenderer renderer = new MarkdownRenderer();

// Convert Markdown to HTML
String html = renderer.renderMarkdown("# Hello Markdown");

// Optionally style it
String styledHtml = renderer.getStyledHtml(
    html,
    new File("README.md"),
    true // true = dark theme
);
```

----

## 🎛 Features
|Method				  |  	Purpose
|--------------------------|-----------------------
|renderMarkdown      	  |	Convert Markdown to HTML
|styledHtml                |	Style the renderer html 

### ✅ Tip: If you don’t have a file context, pass `null` instead of `File`.

---

### 🛠️ Common Operations
- All preview classes (`MarkdownWebView`, `MarkdownTab`, `MarkdownWindow`) support:

|Method				|	Purpose
|------------------------|------------------------------
|`setCurrentFile(File)`	|	Load Markdown from file
|`setContent(String)`		|	Load Markdown from string
|`setDarkMode(boolean)`	|	Enable dark mode
|`dispose()`			|	Release resources
|`isPreviewShowing()`		|	Check visibility
| `updatefullContent()`	|    To refesh the entire preview

--- 

## ✅ All classes support real-time updates when you save the file on disk.

---

- 🏗️ Example Use Cases

	- Embed a Markdown preview inside your JavaFX layout using webview 

	- Show a preview in a standalone window

	- Add a preview tab to a `TabPane`

	- Convert Markdown to styled HTML for export or custom rendering

---

## ❤️ License
- This project is licensed under the [GNU General Public License v3](LICENSE).