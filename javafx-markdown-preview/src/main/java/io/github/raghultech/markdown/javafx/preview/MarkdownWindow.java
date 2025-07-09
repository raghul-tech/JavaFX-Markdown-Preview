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
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.raghultech.markdown.javafx.config.JavaLinkHandler;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewContentException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewFileException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewInitializationException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewRenderException;
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
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import netscape.javascript.JSObject;

public class MarkdownWindow {
	private FileWatcher fileWatcher;
    //private Thread fileWatcherThread;
	  private transient WeakReference<Thread> fileWatcherThread;
    private ExecutorService executor;
    private Stage previewStage;
    private WebEngine engine;
    private Timeline updateTimer;
    private volatile boolean disposed = false;
    private File currentFile ;
    private String OriginalContent;
    private boolean isStringMode = false;
    private String tabName =null;
    private int frameHeight = 600;
    private int frameWidth = 800;
    private Image img;
    private boolean isDark;
    private MarkdownRenderer render = new MarkdownRenderer();
    private JavaFXhandleExternalLinkMailFile handler =  new JavaFXhandleExternalLinkMailFile(); 
    
    public MarkdownWindow(File file) {
    	 if (file == null || !file.exists() || !file.isFile()) {
    	        throw new MarkdownPreviewFileException("Invalid file provided: " + file);
    	    }
    	this.currentFile = file;
    }
    
    public MarkdownWindow(String content) {
    	if (content == null ||content.isEmpty() ) {
            throw new MarkdownPreviewContentException("The String Content should not be null or empty " );
        }
    	this.OriginalContent = content;
    	  this.isStringMode = true;
    }
    
    public void launchPreview() {
    	 try {
    	    //    initializeJavaFX(); 
    	        createExecutor();
    	        setupUpdateTimer();
    	        createAndShowPreviewWindow();
    	        } catch (Exception e) {
    	            dispose(); // Clean up if initialization fails
    	            throw new MarkdownPreviewInitializationException("Failed to initialize Markdown preview window", e);
    	        }
    }
    
    public synchronized void reopenWindow() {
        if (!disposed) {
            dispose();
        }
        
        if (currentFile == null && !isStringMode) {
        	 throw new MarkdownPreviewResourceException("Cannot reopen: no file or string content available.");
            // You could also create and throw a custom InvalidPreviewStateException here
        }

        disposed = false;
        launchPreview();
    }
    
    public boolean isPreviewShowing() {
        //return previewStage != null && previewStage.isShowing();
    	return !disposed;
    }



    private synchronized void createExecutor() {
        if (executor == null || executor.isShutdown()) {
            executor = Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "JavaFXMarkdownPreviewWindow-" + hashCode());
                t.setDaemon(true);
                return t;
            });
        }
    }
   
    private void setupUpdateTimer() {
        if (isStringMode) return;
        updateTimer = new Timeline(new KeyFrame(Duration.millis(700), e -> updatePreviewContent()));
        updateTimer.setCycleCount(1);
    }


    private void createAndShowPreviewWindow() {
        Platform.runLater(() -> {
            try {
            	  if (disposed) return;
            	  
            	  try {
            		  if (previewStage == null) {
            			    previewStage = new Stage();
            			}
            		} catch (Exception e) {
            		    throw new MarkdownPreviewInitializationException("Failed to create JavaFX Stage.", e);
            		}

             //   previewStage.setTitle("Markdown Preview: " + currentFile.getAbsolutePath());
            	  
            	  if(tabName != null) {
            		  previewStage.setTitle(tabName);
            	  }
            	  else if (isStringMode) {
            		  tabName = "Markdown Preview (Raw String)";
                    previewStage.setTitle(tabName);
                 } else {
                	 tabName = "Markdown Preview: " + currentFile.getName();
                    previewStage.setTitle(tabName);
                 }
                
                	
                	try {       
                		if(img == null) {
                	img = new Image(getClass().getResource("/MD.png").toExternalForm());
                		}
                		if (img != null && img.getWidth() > 0) {
                		    previewStage.getIcons().add(img);
                		}               	
                	  //previewStage.getIcons().add(img);
                	}catch(Exception e) {
                		throw new MarkdownPreviewResourceException("Image is invalid or null " + e);
                	}
                	  
                WebView webView = new WebView();
                engine = webView.getEngine();
                
                // Add window listener for close event
                previewStage.setOnCloseRequest(event -> {                 
                	if (!disposed) {
                        dispose();
                    }
                });
                
              
                JavaLinkHandler linkHandler = new JavaLinkHandler(this::handleExternalLink);
                engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED && !disposed) {
                        try {
                            JSObject window = (JSObject) engine.executeScript("window");
                            window.setMember("javaBridge", linkHandler);
                            
                            engine.executeScript(
                                "document.addEventListener('click', function(e) {" +
                                "   var target = e.target.closest('a');" +
                                "   if (target) {" +
                                "       e.preventDefault();" +
                                "       window.javaBridge.handleLink(target.href);" +
                                "   }" +
                                "});"
                            );
                        } catch (Exception e) {
                        	 throw new MarkdownPreviewRenderException("Failed to set up JavaScript bridge.", e);
                        	}
                    }
                });
                
                engine.locationProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal == null || newVal.isEmpty() || newVal.startsWith("data:text/html")) return;
                    Platform.runLater(() -> {
                    	if(!disposed && engine != null) {
                        engine.load(null);
                        handleExternalLink(newVal);
                    	}
                    });
                });
                
                engine.setJavaScriptEnabled(true);
                
                Scene scene = new Scene(webView,frameWidth,frameHeight);
              //  scene.getStylesheets().add(getClass().getResource(getCss()).toExternalForm());
                previewStage.setScene(scene);
                
                previewStage.setOnShown(e -> {
                	   if (!disposed) {
                		   try {
                	          //  updatePreviewContent();
                			   updatefullContent();
                	            if(!isStringMode)watchFileForChanges();
                	        } catch (Exception ex) {
                	            throw new MarkdownPreviewRuntimeException("Failed to initialize preview content or file watcher.", ex);
                	        }
                	   }
                });
                
                previewStage.setOnCloseRequest(this::handleWindowClose);
                
                previewStage.show();
                
            
                
            } catch (Exception e) {
            	 if (!disposed) {
            		 throw new MarkdownPreviewWindowException("Unexpected error during preview window creation.", e);
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
                handler.handleExternalLink(url); 
            });
        } catch (Exception e) {
            if (!disposed) {
                throw new MarkdownPreviewSystemException("Failed to submit external link handling task.", e);
            }
        }
    }


    private  synchronized void watchFileForChanges() {
    	  if (isStringMode) return;
        stopFileWatcher();  // Stop existing watcher before starting a new one
       
        if (currentFile == null || !currentFile.exists()) {
            return;
        }
        
        fileWatcher = new FileWatcher(currentFile);
        fileWatcher.setFileChangeListener(changed -> {
            if (changed && !disposed) {
            	 Platform.runLater(this::triggerPreviewUpdate);
            }
        });
        
        
        
        Thread watcherThread = new Thread(fileWatcher);
        watcherThread.setDaemon(true);
        fileWatcherThread = new WeakReference<>(watcherThread);
        watcherThread.start();

     
    }

    private synchronized void stopFileWatcher() {
        if (fileWatcher != null) {
            fileWatcher.stopWatching(); // Gracefully stop the watcher
        }

        Thread watcherThread = (fileWatcherThread != null) ? fileWatcherThread.get() : null;

        if (watcherThread != null && watcherThread.isAlive()) {
            watcherThread.interrupt();
            try {
                watcherThread.join(1000); // Wait for the thread to stop
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Clear WeakReference to help with garbage collection
        if (fileWatcherThread != null) {
            fileWatcherThread.clear();
        }
    }

    private void triggerPreviewUpdate() {
        if (updateTimer != null) {
            updateTimer.playFromStart();
        }
    }
    
    private void updatePreviewContent() {
        if (engine == null) {
            return;
        }

        String markdown = isStringMode ? OriginalContent : OpenLoom.getContent(currentFile).toString();
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
                Object scrollY = engine.executeScript("window.scrollY || window.pageYOffset");

                // Base64 encode body content
                String encodedBody = java.util.Base64.getEncoder().encodeToString(html.getBytes("UTF-8"));

                // Update body *only* (avoids flicker)
                engine.executeScript(
                    "var decoded = atob('" + encodedBody + "');" +
                    "document.body.innerHTML = decoded;" +
                    "if (typeof hljs !== 'undefined') hljs.highlightAll();"
                );

                // Restore scroll
                if (scrollY instanceof Number) {
                    engine.executeScript(String.format(
                        "window.scrollTo({ top: %d, behavior: 'instant' })",
                        ((Number)scrollY).intValue()
                    ));
                }
            } catch (Exception e) {
                // Fallback: reload full content if anything fails
                engine.loadContent(styledHtml);
            }
        });
    }

    
    public void updatefullContent() {
        if (disposed || engine == null) {
			return;
		}

        String markdown = isStringMode ? OriginalContent : OpenLoom.getContent(currentFile).toString();
        if (markdown == null) {
            throw new MarkdownPreviewResourceException("Content or File is null");
        }


        String html = render.renderMarkdown(markdown);
        String styledHtml = render.getStyledHtml(html, currentFile,isDark);

        Platform.runLater(() -> engine.loadContent(styledHtml));
    }


    private void handleWindowClose(WindowEvent event) {
        dispose();
    }

    public synchronized void dispose() {
        if (disposed) return;
        disposed = true;
        
        // Stop any pending updates
        if (updateTimer != null) {
            updateTimer.stop();
        }
        stopFileWatcher();
        
        // Shutdown executor
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
        Platform.runLater(this::cleanupFxResources);
    }

    private void cleanupFxResources() {
        try {
            if (engine != null) {
                engine.load(null);
            }
        } catch (Exception e) {
           // System.err.println("Error cleaning up WebEngine: " + e.getMessage());
        }
        
        try {
            if (previewStage != null) {
                previewStage.close();
            }
        } catch (Exception e) {
          //  System.err.println("Error closing stage: " + e.getMessage());
        }
        
        // Clear references
        engine = null;
        previewStage = null;
    }
   
    
    public void setIcon(Image image) {
     //   BufferedImage bimage = SwingFXUtils.fromFXImage(fxImage, null);
        this.img = image;
    }
 
    public void setIcon(String resourcePath) {
        this.img = new Image(resourcePath);
    }
    public void setIcon(URL url) {
        this.img = new Image(url.toExternalForm());
    }


    
    public  void setWindowSize(int width, int height) {
    	this.frameWidth = width;
    	this.frameHeight = height;
    }
    
    public int getWindowWidth() { return frameWidth; }
    public int getWindowHeight() { return frameHeight; }
    


    public void setWindowName(String name) { this.tabName = name; }
    public String getWindowName() { return tabName; }
    
    
    public void setCurrentFile(File file) {
    	this.isStringMode = false;
    	this.OriginalContent = null;
    	this.currentFile = file;
    	this.disposed = false;
    	watchFileForChanges();
    	 updatefullContent();
    }
    public File getCurrentFile () { return currentFile; }
    
    public void setContent(String content) { 
    	this.currentFile = null;
    	this.isStringMode = true;
    	this.OriginalContent = content;
    	this.disposed = false;
    	 updatefullContent();
    	}  
    public String getContent() { return OriginalContent; }
    
    public void setDarkMode(boolean isdark) { 
    	this.isDark = isdark;
    	 updatefullContent();
    }
    public boolean getisDarkMode() { return isDark; }

    public boolean isDisposed() {
        return disposed;
    }
    
}