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
package io.github.raghultech.markdown.javafx.integration;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewLinkException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewSystemException;
import io.github.raghultech.markdown.javafx.exception.MarkdownPreviewUnsupportedLinkException;
import javafx.application.HostServices;

public class JavaFXhandleExternalLinkMailFile {


    private HostServices hostServices; // Optional, for JavaFX WebView

    /**
     * Optionally provide HostServices (JavaFX) to support non-Desktop platforms.
     */
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public synchronized void handleExternalLink(String url) {
        try {
        	// if (hostServices != null) {
              //   hostServices.showDocument(url);
                // return;
           //  }
            URI uri = new URI(url);
            String scheme = uri.getScheme().toLowerCase();

            boolean desktopSupported = Desktop.isDesktopSupported();
            Desktop desktop = desktopSupported ? Desktop.getDesktop() : null;

            switch (scheme) {
                case "mailto":
                    if (desktopSupported && desktop.isSupported(Desktop.Action.MAIL)) {
                        handleMailToLink(uri);
                    } else {
                        throw new MarkdownPreviewSystemException("Mailto links are not supported on this system.");
                    }
                    break;
                case "http":
                case "https":
                    if (desktopSupported && desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(uri);
                    } else if (hostServices != null) {
                        hostServices.showDocument(url);
                    } else {
                        throw new MarkdownPreviewSystemException("No way to open HTTP link on this system.");
                    }
                    break;
                case "file":
                    if (desktopSupported && desktop.isSupported(Desktop.Action.OPEN)) {
                        handleFileLink(uri);
                    } else if (hostServices != null) {
                        hostServices.showDocument(url);
                    } else {
                        throw new MarkdownPreviewSystemException("No way to open file link on this system.");
                    }
                    break;
                default:
                    throw new MarkdownPreviewUnsupportedLinkException("Unsupported link scheme: " + scheme);
            }
        } catch (URISyntaxException e) {
            throw new MarkdownPreviewLinkException("Invalid URI syntax: " + url, e);
        } catch (IOException e) {
            throw new MarkdownPreviewLinkException("I/O error while opening link: " + url, e);
        } catch (Exception e) {
            throw new MarkdownPreviewLinkException("Unexpected error while handling link: " + url, e);
        }
    }

    private void handleMailToLink(URI uri) throws Exception {
    	try {
        if (Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
            String mailTo = uri.toString()
                .replace(" ", "%20")
                .replace("+", "%2B");
            Desktop.getDesktop().mail(new URI(mailTo));
        }  else {
        	   fallbackMail(uri.toString());
           // throw new MarkdownPreviewSystemException("Mailto links are not supported on this system.");
        }
    } catch (Exception e) {
        throw new MarkdownPreviewLinkException("Failed to handle mailto link: " + uri, e);
    }
    }

    private void fallbackMail(String mailto) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            new ProcessBuilder("open", mailto).start();
        } else if (os.contains("nix") || os.contains("nux")) {
            new ProcessBuilder("xdg-email", mailto).start();
        } else {
        	  throw new MarkdownPreviewSystemException("Mailto links are not supported on this system.");
            //throw new IOException("No fallback mail handler.");
        }
    }

    
    private void handleFileLink(URI uri) {
        try {
            File file = Paths.get(uri).toFile();
            if (file.exists()) {
                if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    Desktop.getDesktop().open(file);
                } else if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(file.toURI());
                } else if (hostServices != null) {
                    hostServices.showDocument(file.toURI().toString());
                } else {
                	  fallbackOpen(file);
                  //  throw new MarkdownPreviewSystemException("No way to open the file.");
                }
            } else {
                throw new MarkdownPreviewLinkException("File not found: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            throw new MarkdownPreviewLinkException("Failed to handle file link: " + uri, e);
        }
    }

    private void fallbackOpen(File file) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            new ProcessBuilder("explorer", file.getAbsolutePath()).start();
        } else if (os.contains("mac")) {
            new ProcessBuilder("open", file.getAbsolutePath()).start();
        } else if (os.contains("nix") || os.contains("nux")) {
            new ProcessBuilder("xdg-open", file.getAbsolutePath()).start();
        } else {
        	 throw new MarkdownPreviewSystemException("No way to open the file.");
          //  throw new IOException("Cannot determine OS to open file.");
        }
    }

    
}
