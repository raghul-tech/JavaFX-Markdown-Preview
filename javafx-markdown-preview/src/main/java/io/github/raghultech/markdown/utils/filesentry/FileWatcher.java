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
package io.github.raghultech.markdown.utils.filesentry;

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

