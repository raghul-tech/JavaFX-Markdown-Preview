
package com.raghultech.markdown.filewatcher;

import java.io.File;

public class FileWatcher implements Runnable {
    
    private final Object fileWatcher; // Generic Object to hold either Windows or Linux watcher

    public FileWatcher(File file) {
      
        String os = System.getProperty("os.name").toLowerCase();
      //  fileWatcher = new LinuxFileWatcher(file, textArea);
        if (os.contains("win")) {
            fileWatcher = new WindowsFileWatcher(file);
        } else  {
            fileWatcher = new LinuxFileWatcher(file);
        } 
    }

 
    public interface FileChangeListener {
        void onFileChangeDetected(boolean changed);
    }
    
    public void setFileChangeListener(FileChangeListener listener) {
        if (fileWatcher instanceof WindowsFileWatcher) {
            ((WindowsFileWatcher) fileWatcher).setFileChangeListener(listener);
        } else if (fileWatcher instanceof LinuxFileWatcher) {
            ((LinuxFileWatcher) fileWatcher).setFileChangeListener(listener);
        }
        //System.err.println("FIle watcher set saving is "+ saving);
        
    }

    public void stopWatching() {
        if (fileWatcher instanceof WindowsFileWatcher) {
            ((WindowsFileWatcher) fileWatcher).stopWatching();
        } else if (fileWatcher instanceof LinuxFileWatcher) {
            ((LinuxFileWatcher) fileWatcher).stopWatching();
        }
    }

    @Override
    public void run() {
        if (fileWatcher instanceof WindowsFileWatcher) {
            ((WindowsFileWatcher) fileWatcher).run();
        } else if (fileWatcher instanceof LinuxFileWatcher) {
            ((LinuxFileWatcher) fileWatcher).run();
        }
    }
}

