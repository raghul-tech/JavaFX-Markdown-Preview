package com.raghultech.markdown.filewatcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import com.raghultech.markdown.filewatcher.FileWatcher.FileChangeListener;

public class WindowsFileWatcher implements Runnable {
    private final File file;
  
    private volatile boolean keepWatching = true;
    private static final long DEBOUNCE_DELAY = 1000; // 1 second debounce delay
    private long lastChangeTime = 0; // Last time a change was detected
    private WatchService watchService;
    private ScheduledExecutorService executorService;
    private FileChangeListener changeListener;
    
    public WindowsFileWatcher(File file) {
        this.file = file;
        
    }

    @Override
    public void run() {
        try {
        	
        	File absoluteFile = file.getAbsoluteFile(); // ensures parent is not null
        	File parentFile = absoluteFile.getParentFile();
             if (parentFile == null)   return;
            watchService = FileSystems.getDefault().newWatchService();
            Path filePath = parentFile.toPath();
          //  filePath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            filePath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE);
            
            // Using ScheduledExecutorService for debounce handling
            executorService = Executors.newSingleThreadScheduledExecutor();

            while (keepWatching) {
            	try {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                 //   if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    if ((kind == StandardWatchEventKinds.ENTRY_MODIFY || kind == StandardWatchEventKinds.ENTRY_CREATE
                            || kind == StandardWatchEventKinds.ENTRY_DELETE) ) {
                        Path changed = filePath.resolve((Path) event.context());
                        if (changed.endsWith(file.getName())) {
                            long currentTime = System.currentTimeMillis();
                            if ( currentTime - lastChangeTime > DEBOUNCE_DELAY) {
                                lastChangeTime = currentTime;
           
                                    scheduleFileReload();
                                
                            }
                        }
                    }
                }
                if (!key.reset()) break;
            }
            	  catch (ClosedWatchServiceException e) {
                     // System.out.println("WatchService was closed, stopping watcher.");
                      break; // Exit loop safely
                  }
            }
        }
        catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        finally {
            stopWatching();
        }
    }
    
    // Schedule file reload with debounce
    private void scheduleFileReload() {
        if (executorService != null) {
            executorService.schedule(() -> SwingUtilities.invokeLater(this::reloadFile), 500, TimeUnit.MILLISECONDS);
        }
    }
    
    
  

    public void setFileChangeListener(FileChangeListener listener) {
        this.changeListener = listener;
    }
    
    
    private void reloadFile() {
        if (!file.exists()) return;
        if (changeListener != null) {
            changeListener.onFileChangeDetected(true);
        }
    }

  
    public void stopWatching() {
        keepWatching = false;
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        try {
            if (watchService != null) {
                watchService.close();
            }
        } catch (IOException e) {
            // Handle WatchService closing error
        }
    }
}
