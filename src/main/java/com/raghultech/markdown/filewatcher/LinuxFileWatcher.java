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

public class LinuxFileWatcher implements Runnable {
    private final File file;

    private volatile boolean keepWatching = true;
    private volatile boolean isReloading = false;
    private static final long DEBOUNCE_DELAY = 1000; // 1 second debounce delay
    private static final long POLL_INTERVAL = 1000; // 1 second polling interval
    private WatchService watchService;
    private ScheduledExecutorService executorService;
    private long lastChangeTime = 0; // Last detected modification time
    private volatile long lastSavedTime = 0;
    private FileChangeListener changeListener;
  //  private final boolean isLinux;

    public LinuxFileWatcher(File file) {
        this.file = file;
    //   this.isLinux = System.getProperty("os.name").toLowerCase().contains("linux");
    }

    @Override
    public void run() {
        try {
        	File absoluteFile = file.getAbsoluteFile(); // ensures parent is not null
        	File parentFile = absoluteFile.getParentFile();
            if (parentFile == null)   return;
        	this.lastChangeTime = file.lastModified();
        	this.lastSavedTime = file.lastModified();

            watchService = FileSystems.getDefault().newWatchService();
            Path filePath = parentFile.toPath();
            filePath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,
                                            StandardWatchEventKinds.ENTRY_CREATE,
                                            StandardWatchEventKinds.ENTRY_DELETE);

            executorService = Executors.newScheduledThreadPool(2);

            // Polling fallback for Linux
        //    if (isLinux) {
                executorService.scheduleAtFixedRate(this::pollForChanges, 0, POLL_INTERVAL, TimeUnit.MILLISECONDS);
          //  }

            // WatchService event handling
            while (keepWatching) {
            	try {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if ((kind == StandardWatchEventKinds.ENTRY_MODIFY || kind == StandardWatchEventKinds.ENTRY_CREATE
                            || kind == StandardWatchEventKinds.ENTRY_DELETE)) {
                        Path changed = filePath.resolve((Path) event.context());
                        if (changed.endsWith(file.getName())) {
                          //  long currentTime = System.currentTimeMillis();
                        	long currentTime = file.lastModified();
                            if (currentTime - lastChangeTime > DEBOUNCE_DELAY) {
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
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            stopWatching();
        }
    }

    private void pollForChanges() {
        if (!file.exists() ) return;

        long lastModified = file.lastModified();

        if (lastModified > lastChangeTime &&
            lastModified > lastSavedTime + DEBOUNCE_DELAY) {

            lastChangeTime = lastModified;
            scheduleFileReload();
        }
    }


    private void scheduleFileReload() {
        if (executorService != null && !executorService.isShutdown() && !isReloading) {
            isReloading = true;
            executorService.schedule(() -> SwingUtilities.invokeLater(() -> {
                reloadFile();
                isReloading = false;
            }), 500, TimeUnit.MILLISECONDS);
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
            e.printStackTrace();
        }
    }
}
