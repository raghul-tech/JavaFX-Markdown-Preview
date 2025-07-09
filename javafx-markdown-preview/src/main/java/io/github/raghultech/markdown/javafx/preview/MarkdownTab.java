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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.github.raghultech.markdown.javafx.config.JavaLinkHandler;
import io.github.raghultech.markdown.javafx.config.PreviewContent;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewContentException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewFileException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewInitializationException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewResourceException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewRuntimeException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewSystemException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewWindowException;
import io.github.raghultech.markdown.javafx.integration.JavaFXhandleExternalLinkMailFile;
import io.github.raghultech.markdown.javafx.integration.MarkdownRenderer;
import io.github.raghultech.markdown.utils.filesentry.FileWatcher;
import io.github.raghultech.markdown.utils.openloom.OpenLoom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import netscape.javascript.JSObject;


public class MarkdownTab {
    
    private Tab previewTab;
    private Timeline updateTimer;
    private final Map<Object, PreviewContent> previewContents = new HashMap<>();
    private ExecutorService executor;
    private File currentFile;
    private String originalContent;
    private boolean isStringMode = false;
    private FileWatcher fileWatcher;
    private transient WeakReference<Thread> fileWatcherThread;
    private String tabName = null;
    private Image tabIcon ;
    private int iconSize = 16;
    private Button closeButton;
    private volatile boolean disposed = false;
    private final TabPane parentTabPane;
   // private WebEngine currentEngine;
    private  HostServices hostServices = null;
    private boolean isDark;
    private MarkdownRenderer render = new MarkdownRenderer();
    private JavaFXhandleExternalLinkMailFile handler =  new JavaFXhandleExternalLinkMailFile(); 
    
    
 // Constructor for file-based preview with existing Tab
    public MarkdownTab(TabPane parentTabPane, File file) {
        validateParameters(parentTabPane, file);
        this.parentTabPane = parentTabPane;
        this.currentFile = file;
        createNewPreviewTab();
    }

    // Constructor for string-based preview with existing Tab
    public MarkdownTab(TabPane parentTabPane, String content) {
        validateParameters(parentTabPane, content);
        this.parentTabPane = parentTabPane;
        this.originalContent = content;
        this.isStringMode = true;
        createNewPreviewTab();
    }

    // Constructor for file-based preview with custom Tab
    public MarkdownTab(Tab existingTab, File file) {
        validateParameters(existingTab, file);
        this.previewTab = existingTab;
        this.parentTabPane = existingTab.getTabPane();
        this.currentFile = file;
    }

    // Constructor for string-based preview with custom Tab
    public MarkdownTab(Tab existingTab, String content) {
        validateParameters(existingTab, content);
        this.previewTab = existingTab;
        this.parentTabPane = existingTab.getTabPane();
        this.originalContent = content;
        this.isStringMode = true;
    }

    
    private void validateParameters(Object tab, Object content) {
        if (tab == null) {
            throw new MarkdownPreviewInitializationException("Tab/TabPane should not be null");
        }
        if (content == null || (content instanceof File && !((File)content).exists())) {
            throw content instanceof File ? 
                new MarkdownPreviewFileException("Invalid file provided") :
                new MarkdownPreviewContentException("Content should not be null or empty");
        }
    }

    private void createNewPreviewTab() {
        this.previewTab = new Tab();
        if (parentTabPane != null) {
            int insertIndex;
            Tab selectedTab = parentTabPane.getSelectionModel().getSelectedItem();

            if (selectedTab != null) {
                int selectedIndex = parentTabPane.getTabs().indexOf(selectedTab);
                if (selectedIndex >= 0) {
                    insertIndex = selectedIndex + 1; // after selected
                } else {
                    insertIndex = parentTabPane.getTabs().size(); // fallback: append
                }
            } else {
                insertIndex = 0; // no tabs at all: insert as first
            }

            parentTabPane.getTabs().add(insertIndex, previewTab);
        }
    }



    public void launchTab() {
    	 if (disposed) {
             throw new MarkdownPreviewRuntimeException("Preview has been disposed");
         }
        try {
          //  initializeJavaFX();
            createExecutor();
            setupUpdateTimer();
            setupPreviewContent();
        } catch (Exception e) {
            dispose();
            throw new MarkdownPreviewInitializationException("Failed to initialize preview", e);
        }
    }
    
    
    public void relaunchTab() {
    	 if (!disposed) {
    	     dispose();
    	 }
    	 
    	 if (currentFile == null && !isStringMode) {
    	     throw new MarkdownPreviewResourceException("Cannot reopen: no file or string content available.");
    	    
    	 }
    	 
    	 disposed = false;
    	 try {
    	 createExecutor();
         setupPreviewContent();
    	 } catch (Exception e) {
             dispose();
             throw new MarkdownPreviewInitializationException("Failed to initialize preview", e);
         }
    }

    private void setupUpdateTimer() {
        if (isStringMode) return;
        updateTimer = new Timeline(new KeyFrame(Duration.millis(700), e -> updatePreviewContent()));
        updateTimer.setCycleCount(1);
    }

    private void createExecutor() {
        if (executor == null || executor.isShutdown()) {
            this.executor = Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "JavaFXMarkdownPreviewTabPane - " + hashCode());
                t.setDaemon(true);
                return t;
            });
        }
    }

    private void setupPreviewContent() {
    	 if (disposed) {
    	        return;
    	    }
        Object key = getPreviewKey();
        try {
        if (previewContents.containsKey(key)) {
        	 cleanupPreviewResources(key, previewContents.get(key).webView);
        	//return;
        }

        String markdown = isStringMode ? originalContent : OpenLoom.getContent(currentFile).toString();
        String html = render.renderMarkdown(markdown)
        	    .replace(":emoji_name:", "<img class='emoji' alt=':emoji_name:' src='https://github.githubassets.com/images/icons/emoji/unicode/1f604.png' height='20' width='20'>");
        String styledHtml = render.getStyledHtml(html, currentFile,isDark);

        Platform.runLater(() -> {
        	  if (disposed) return;
            try {
            	   WebView webView = previewContents.containsKey(key) ? 
                           previewContents.get(key).webView : new WebView();
                WebEngine engine = webView.getEngine();
              //  this.currentEngine = engine;
                engine.setJavaScriptEnabled(true);

                // Setup link handling
             // In setupPreviewContent() method:
                engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED && !disposed) {
                        try {
                            // More robust link interception
                            engine.executeScript(
                                "document.body.addEventListener('click', function(e) {" +
                                "   var target = e.target;" +
                                "   while (target && target.nodeName !== 'A') {" +
                                "       target = target.parentNode;" +
                                "       if (!target) return true;" +
                                "   }" +
                                "   if (target && target.href) {" +
                                "       e.preventDefault();" +
                                "       window.java.handleLink(target.href);" +
                                "       return false;" +
                                "   }" +
                                "   return true;" +
                                "});"
                            );
                            
                            // Set up Java bridge
                            JSObject window = (JSObject) engine.executeScript("window");
                            window.setMember("java", new JavaLinkHandler(url -> {
                                Platform.runLater(() -> handleExternalLink(url));
                            }));
                        } catch (Exception e) {
                            System.err.println("Link handling setup failed: " + e.getMessage());
                        }
                    }
                });

                // Apply dark mode styles if needed
                applyStyles(engine);

                // Set the content
                engine.loadContent(styledHtml);

                // Store the preview components
                previewContents.put(key, new PreviewContent(webView, engine));
                
             // Configure tab if not already configured
                if (previewTab.getContent() == null) {
                    setupTabHeader(webView, key);
                    previewTab.setContent(webView);
                }
                
                // Start file watching if needed
                if (!isStringMode && !disposed) {
                    watchFileForChanges();
                }


            } catch (Exception e) {
            	 cleanupPreviewResources(key, null);
                 if (!disposed) {
                     throw new MarkdownPreviewWindowException("Failed to create preview", e);
                 }
            }
        });
    }catch (Exception e) {
        cleanupPreviewResources(key, null);
        if (!disposed) {
            throw new MarkdownPreviewWindowException("Failed to setup preview content", e);
        }
    }
}
    
    
    
    private void setupTabHeader(WebView webView, Object key) {
        HBox tabHeader = new HBox();
        tabHeader.setSpacing(5);
        
        // Add icon if set
        if (tabIcon != null) {
            ImageView iconView = new ImageView(tabIcon);
            iconView.setFitWidth(iconSize);
            iconView.setFitHeight(iconSize);
            tabHeader.getChildren().add(iconView);
        }
        
        // Add title
       // Label titleLabel = new Label(getTabTitle());
        //tabHeader.getChildren().add(titleLabel);
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        tabHeader.getChildren().add(spacer);
        
        // Add close button (custom or default)
        Button closeBtn = closeButton != null ? closeButton : null;
     if(closeBtn!=null)  tabHeader.getChildren().add(closeBtn);
        
        previewTab.setGraphic(tabHeader);
        previewTab.setContent(webView);
    }
 

  /*  private String getTabTitle() {
        return tabName != null ? tabName : 
               isStringMode ? "Preview " : "Preview: " + currentFile.getName();
    }*/


    
    
    private void applyStyles(WebEngine engine) {
        boolean isDarkMode = false;
        String scrollbarCss = "::-webkit-scrollbar { width: 10px; }" +
            "::-webkit-scrollbar-track { background: " + (isDarkMode ? "#2b2b2b" : "#f1f1f1") + "; }" +
            "::-webkit-scrollbar-thumb { background: " + (isDarkMode ? "#555555" : "#888") + "; }" +
            "::-webkit-scrollbar-thumb:hover { background: " + (isDarkMode ? "#666666" : "#555") + "; }";
        
        engine.setUserStyleSheetLocation("data:text/css;charset=utf-8," + scrollbarCss);
    }

    private void watchFileForChanges() {
        stopFileWatcher();
        
        if (currentFile == null || !currentFile.exists()) {
            return;
        }
        
        fileWatcher = new FileWatcher(currentFile);
        fileWatcher.setFileChangeListener(changed -> {
            if (changed) {
                //updatePreviewContent();
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

    private void updatePreviewContent() {
    	 Object key = getPreviewKey();
         PreviewContent content = previewContents.get(key);
         if (content == null || disposed) {
             return;
         }

        String markdown = isStringMode ? originalContent : OpenLoom.getContent(currentFile).toString();
        if (markdown == null) {
            throw new MarkdownPreviewContentException("Content or File is null");
        }

        // Render markdown to HTML
        String html = render.renderMarkdown(markdown)
                .replace(":emoji_name:", "<img class='emoji' alt=':emoji_name:' src='https://github.githubassets.com/images/icons/emoji/unicode/1f604.png' height='20' width='20'>");
        String styledHtml = render.getStyledHtml(html, currentFile, isDark);

        Platform.runLater(() -> {
            try {
                // Save scroll position
                Object scrollY = content.engine.executeScript("window.scrollY || window.pageYOffset");

                // Base64 encode body content
                String encodedBody = java.util.Base64.getEncoder().encodeToString(html.getBytes("UTF-8"));

                // Update body *only* (avoids flicker)
                content.engine.executeScript(
                    "var decoded = atob('" + encodedBody + "');" +
                    "document.body.innerHTML = decoded;" +
                    "if (typeof hljs !== 'undefined') hljs.highlightAll();"
                );

                // Restore scroll
                if (scrollY instanceof Number) {
                	content.engine.executeScript(String.format(
                        "window.scrollTo({ top: %d, behavior: 'instant' })",
                        ((Number)scrollY).intValue()
                    ));
                }
            } catch (Exception e) {
                // Fallback: reload full content if anything fails
            	content.engine.loadContent(styledHtml);
            }
        });
    }
    
    public void updatefullContent() {
    	 Object key = getPreviewKey();
         PreviewContent content = previewContents.get(key);
         if (content == null || disposed) {
             return;
         }

        String markdown = isStringMode ? originalContent : OpenLoom.getContent(currentFile).toString();
        if (markdown == null) {
            throw new MarkdownPreviewResourceException("Content or File is null");
        }


        String html = render.renderMarkdown(markdown);
        String styledHtml = render.getStyledHtml(html, currentFile,isDark);

        Platform.runLater(() -> content.engine.loadContent(styledHtml));
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
    
   
    
  
	private void cleanupPreviewResources(Object key, WebView webView) {
        // 1. Stop file watcher if this was a file preview
        if (key instanceof File) {
            stopFileWatcher();
        }

        // 2. Get our specific preview content
        PreviewContent previewContent = previewContents.get(key);
        if (previewContent == null) return;

        // 3. Clean up only our WebEngine
        WebEngine engine = previewContent.engine;
        if (engine != null) {
            Platform.runLater(() -> {
                try {
                    // Cancel any pending loads
                    if (engine.getLoadWorker().isRunning()) {
                        engine.getLoadWorker().cancel();
                    }
                    
                    // Clear our JavaScript bridge only
                    try {
                        engine.executeScript("if(window.javaBridge) { delete window.javaBridge; }");
                    } catch (Exception jsEx) {
                        // Ignore if JS context already gone
                    }
                    
                    // Load blank page (only affects our WebView)
                    engine.load("about:blank");
                } catch (Exception e) {
                    System.err.println("Error cleaning up engine: " + e.getMessage());
                }
            });
        }

        // 4. Remove only our reference from tracking map
        previewContents.remove(key);
    }

    private void updateTabTitle() {
        if (tabName != null) {
            previewTab.setText(tabName);
        } else {
            previewTab.setText(isStringMode ? "Markdown Preview" : "Preview: " + currentFile.getName());
        }
    }

    public void dispose() {
        if (disposed) return;
        disposed = true;
        
        stopFileWatcher();
        
        if (executor != null) {
            executor.shutdownNow();
            try {
                executor.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        Platform.runLater(() -> {
            previewContents.values().forEach(content -> {
                content.engine.load("about:blank");
            });
            previewContents.clear();
            
            if (previewTab.getTabPane() != null) {
                previewTab.getTabPane().getTabs().remove(previewTab);
            }
        });
 
    }

   
    /**
     * Sets a custom close button for the tab while preserving any existing actions
     * @param button The button to use (null to use default)
     */
    public void setCloseButton(Button button) {
        // Remove the old close button action if it exists
        if (this.closeButton != null) {
            this.closeButton.setOnAction(null);
        }
        
        this.closeButton = button;
        if (this.closeButton != null) {
            // Get any existing action
            javafx.event.EventHandler<javafx.event.ActionEvent> existingAction = 
                this.closeButton.getOnAction();
                
            // Set new action that runs both existing and dispose
            this.closeButton.setOnAction(e -> {
                try {
                    // Run existing action if present
                    if (existingAction != null) {
                        existingAction.handle(e);
                    }
                } finally {
                    // Always dispose
                    dispose();
                }
            });
        }
        updateTabHeader();
    }
    
    private void updateTabHeader() {
        Object key = getPreviewKey();
        PreviewContent content = previewContents.get(key);
        if (content != null) {
            Platform.runLater(() -> setupTabHeader(content.webView, key));
        }
    }
    public void setTabIcon(Image icon, int size) {
        this.tabIcon = icon;
        this.iconSize = size;
        updateTabHeader();
    }
    
    public Image getTabIcon() {
        return tabIcon;
    }
    
    public int getIconSize() {
        return iconSize;
    }
    
    public void setIconSize(int size) {
        this.iconSize = size;
        updateTabHeader();
    }
    public Button getCloseButton() {
        return closeButton;
    }
    
    public boolean isPreviewShowing() {
        //return previewStage != null && previewStage.isShowing();
    	return !disposed;
    }
    
    public boolean isDisposed() {
        return disposed;
    }
    
    public void setTabName(String name) {
        this.tabName = name;
        Platform.runLater(this::updateTabTitle);
    }
    
    public String getTabName() {
        return tabName;
    }

    private Object getPreviewKey() {
        return isStringMode ? this : currentFile;
    }
    
    public void setCurrentFile(File file) {
    	this.isStringMode = false;
    	this.originalContent = null;
    	this.currentFile = file;
    	this.disposed = false;
    	updatefullContent();
    }
    public File getCurrentFile () { return currentFile; }
    
    public void setContent(String content) { 
    	this.currentFile = null;
    	this.isStringMode = true;
    	this.originalContent = content;
    	this.disposed = false;
    	updatefullContent();
    	}  
    public String getContent() { return originalContent; }
    
    public void setDarkMode(boolean isdark) { 
    	this.isDark = isdark;
    	 updatefullContent();
    }
    public boolean getisDarkMode() { return isDark; }

    public HostServices getHostServices() {
  		// TODO Auto-generated method stub
  		return hostServices;
  	}
      
      public void setHostServices(  HostServices hostServices) {
      	this.hostServices = hostServices;
      }

      
}