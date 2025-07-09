/*
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
 */
package io.github.raghultech.markdown.javafx.preview;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.github.raghultech.markdown.javafx.config.JavaLinkHandler;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewContentException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewFileException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewInitializationException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewResourceException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewRuntimeException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewSystemException;
import io.github.raghultech.markdown.javafx.integration.JavaFXhandleExternalLinkMailFile;
import io.github.raghultech.markdown.javafx.integration.MarkdownRenderer;
import io.github.raghultech.markdown.utils.filesentry.FileWatcher;
import io.github.raghultech.markdown.utils.openloom.OpenLoom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import netscape.javascript.JSObject;

/**
 * A reusable Markdown preview component that renders into a WebView.
 * You can:
 * - Supply your own WebView or let it create one.
 * - Render content from File or String.
 * - Attach HostServices to open external links.
 */
public class MarkdownWebView {

    private final WebView webView;
    private final WebEngine webEngine;
    private final HostServices hostServices;
    private final ExecutorService executor;
    

    private File markdownFile;
    private String markdownContent;
    private Timeline updateTimer;
    private boolean disposed = false;
    private boolean isStringMode = false;
    private boolean isDark;

    private FileWatcher fileWatcher;
    private transient WeakReference<Thread> fileWatcherThread;
    
    private MarkdownRenderer render = new MarkdownRenderer();
    private JavaFXhandleExternalLinkMailFile handler =  new JavaFXhandleExternalLinkMailFile(); 
    
    /**
     * Constructor with File input and new WebView.
     */
    public MarkdownWebView(File file, HostServices hostServices) {
      //  this(new WebView(), file, hostServices);
        this.webView = new WebView();
        this.webEngine = this.webView.getEngine();
        this.hostServices = hostServices;
        this.markdownFile = file;
        this.isStringMode = false;
        this.executor = createExecutor();
        validateParams(webView, file, hostServices);
    }

    /**
     * Constructor with String input and new WebView.
     */
    public MarkdownWebView(String content, HostServices hostServices) {
       // this(new WebView(), content, hostServices);
    	   this.webView = new WebView();
           this.webEngine = this.webView.getEngine();
           this.hostServices = hostServices;
           this.markdownContent = content;
           this.isStringMode = true;
           this.executor = createExecutor();
           validateParams(webView, content, hostServices);
    }

    /**
     * Constructor with supplied WebView and File.
     */
    public MarkdownWebView(WebView webView, File file, HostServices hostServices) {
        validateParams(webView, file, hostServices);
        this.webView = webView;
        this.webEngine = webView.getEngine();
        this.hostServices = hostServices;
        this.markdownFile = file;
        this.isStringMode = false;
        this.executor = createExecutor();
    }

    /**
     * Constructor with supplied WebView and String.
     */
    public MarkdownWebView(WebView webView, String content, HostServices hostServices) {
        validateParams(webView, content, hostServices);
        this.webView = webView;
        this.webEngine = webView.getEngine();
        this.hostServices = hostServices;
        this.markdownContent = content;
        this.isStringMode = true;
        this.executor = createExecutor();
    }

    private void validateParams(Object view, Object content, HostServices services) {
        if (view == null) throw new MarkdownPreviewInitializationException("WebView must not be null.");
        if (services == null) throw new MarkdownPreviewInitializationException("HostServices must not be null.");
        if (content == null || (content instanceof File && !((File) content).exists())) {
            throw content instanceof File
                    ? new MarkdownPreviewFileException("Invalid file provided.")
                    : new MarkdownPreviewContentException("Content must not be null or empty.");
        }
    }

    private ExecutorService createExecutor() {
        return Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "JavaFXMarkdownWebViewPreview-" + hashCode());
            t.setDaemon(true);
            return t;
        });
    }
    private void setupUpdateTimer() {
        if (isStringMode) return;
        updateTimer = new Timeline(new KeyFrame(Duration.millis(700), e -> updatePreviewContent()));
        updateTimer.setCycleCount(1);
    }

    /**
     * Launches the preview.
     */
    public WebView launch() {
        if (disposed) throw new MarkdownPreviewRuntimeException("Preview has been disposed.");
        setupUpdateTimer();
        String markdown;
        try {
            markdown = isStringMode
                    ? markdownContent
                    : OpenLoom.getContent(markdownFile).toString();
        } catch (Exception e) {
            throw new MarkdownPreviewFileException("Failed to read markdown content.", e);
        }

        String html = render.renderMarkdown(markdown)
        	    .replace(":emoji_name:", "<img class='emoji' alt=':emoji_name:' src='https://github.githubassets.com/images/icons/emoji/unicode/1f604.png' height='20' width='20'>");
        String styledHtml = render.getStyledHtml(html, markdownFile,isDark);

        Platform.runLater(() -> {
            webEngine.setJavaScriptEnabled(true);
            setupLinkHandler();
            webEngine.loadContent(styledHtml);
            if (!isStringMode && !disposed) {
                watchFileForChanges();
            }

        });
        return webView;
    }


	private void setupLinkHandler() {
        webEngine.getLoadWorker().stateProperty().addListener((obs, old, state) -> {
            if (state == Worker.State.SUCCEEDED && !disposed) {
                try {
                    webEngine.executeScript(
                        "document.body.addEventListener('click', function(e) {" +
                        "  var target = e.target;" +
                        "  while (target && target.nodeName !== 'A') { target = target.parentNode; }" +
                        "  if (target && target.href) {" +
                        "    e.preventDefault();" +
                        "    window.java.handleLink(target.href);" +
                        "  }" +
                        "});"
                    );
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    window.setMember("java", new JavaLinkHandler(url -> {
                        Platform.runLater(() -> handleExternalLink(url));
                    }));
                } catch (Exception e) {
                    System.err.println("Error setting up link handling: " + e.getMessage());
                }
            }
        });
    }
    private void handleExternalLink(String url) {
        if (disposed) return;

        try {
            if (executor == null || executor.isShutdown()) {
                createExecutor();
            }

            executor.execute(() -> {
                if (disposed) return;
            handler.setHostServices(hostServices);
            handler.handleExternalLink(url);
            });
        } catch (Exception e) {
            if (!disposed) {
                throw new MarkdownPreviewSystemException("Failed to submit external link handling task.", e);
            }
        }
    }
    
    
    private void watchFileForChanges() {
        stopFileWatcher();
        
        if( markdownFile == null || !markdownFile.exists()) {
            return;
        }
        
        fileWatcher = new FileWatcher(markdownFile);
        fileWatcher.setFileChangeListener(changed -> {
            if (changed) {
               // updatePreviewContent();
                Platform.runLater(this::triggerPreviewUpdate);
            	 
            }
        });
        
        Thread watcherThread = new Thread(fileWatcher);
        watcherThread.setDaemon(true);
        fileWatcherThread = new WeakReference<>(watcherThread);
        watcherThread.start();
    }

    private void stopFileWatcher() {
        if (fileWatcher != null) {
            fileWatcher.stopWatching();
        }

        Thread watcherThread = fileWatcherThread != null ? fileWatcherThread.get() : null;
        if (watcherThread != null && watcherThread.isAlive()) {
            watcherThread.interrupt();
            try {
                watcherThread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private void triggerPreviewUpdate() {
        if (updateTimer != null) {
            updateTimer.playFromStart();
        }
    }
    
    /**
     * Reloads the markdown file and updates the preview content.
     */
    private void updatePreviewContent() {
        if (webEngine == null) {
            return;
        }

        String markdown = isStringMode ? markdownContent : OpenLoom.getContent(markdownFile).toString();
        if (markdown == null) {
            throw new MarkdownPreviewContentException("Content or File is null");
        }

        // Render markdown to HTML
        String html = render.renderMarkdown(markdown)
                .replace(":emoji_name:", "<img class='emoji' alt=':emoji_name:' src='https://github.githubassets.com/images/icons/emoji/unicode/1f604.png' height='20' width='20'>");
        String styledHtml = render.getStyledHtml(html, markdownFile, isDark);

        Platform.runLater(() -> {
            try {
                // Save scroll position
                Object scrollY = webEngine.executeScript("window.scrollY || window.pageYOffset");

                // Base64 encode body content
                String encodedBody = java.util.Base64.getEncoder().encodeToString(html.getBytes("UTF-8"));

                // Update body *only* (avoids flicker)
                webEngine.executeScript(
                    "var decoded = atob('" + encodedBody + "');" +
                    "document.body.innerHTML = decoded;" +
                    "if (typeof hljs !== 'undefined') hljs.highlightAll();"
                );

                // Restore scroll
                if (scrollY instanceof Number) {
                    webEngine.executeScript(String.format(
                        "window.scrollTo({ top: %d, behavior: 'instant' })",
                        ((Number)scrollY).intValue()
                    ));
                }
            } catch (Exception e) {
                // Fallback: reload full content if anything fails
                webEngine.loadContent(styledHtml);
            }
        });
    }

    
    public void updatefullContent() {
        if (disposed || webEngine == null) {
			return;
		}

        String markdown = isStringMode ? markdownContent : OpenLoom.getContent(markdownFile).toString();
        if (markdown == null) {
            throw new MarkdownPreviewResourceException("Content or File is null");
        }


        String html = render.renderMarkdown(markdown);
        String styledHtml = render.getStyledHtml(html, markdownFile,isDark);

        Platform.runLater(() -> webEngine.loadContent(styledHtml));
    }


    

    /**
     * Disposes resources and stops the preview.
     */
    public void dispose() {
        if (disposed) return;
        disposed = true;

        executor.shutdownNow();
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Platform.runLater(() -> webEngine.load("about:blank"));
    }

    /**
     * Returns the WebView to embed in your layout.
     */
    public WebView getWebView() {
        return webView;
    }

    /**
     * Indicates whether the preview has been disposed.
     */
    public boolean isDisposed() {
        return disposed;
    }
    
    public boolean isPreviewShowing() {
        //return previewStage != null && previewStage.isShowing();
    	return !disposed;
    }
    
    public void setCurrentFile(File file) {
    	this.isStringMode = false;
    	this.markdownContent = null;
    	this.markdownFile = file;
    	this.disposed = false;
    	watchFileForChanges();
    	 updatefullContent();
    }
    public File getCurrentFile () { return markdownFile; }
    
    public void setContent(String content) { 
    	this.markdownFile = null;
    	this.isStringMode = true;
    	this.markdownContent = content;
    	this.disposed = false;
    	 updatefullContent();
    	}  
    public String getContent() { return markdownContent; }

    public void setDarkMode(boolean isdark) { 
    	this.isDark = isdark;
    	 updatefullContent();
    }
    public boolean getisDarkMode() { return isDark; }
    
}
