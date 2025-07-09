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

import io.github.raghultech.markdown.javafx.preview.MarkdownWebView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;

/**
 * ExampleScenePreview demonstrates how to embed the MarkdownWebView
 * inside a JavaFX Scene. It loads a markdown file (README.md)
 * and displays it in a window.
 */
public class ExampleScenePreview extends Application {

    /**
     * The MarkdownWebView instance used to render markdown content.
     */
    private MarkdownWebView preview;

    /**
     * Called automatically by the JavaFX runtime when the application starts.
     * Sets up the primary Stage and displays the markdown content.
     *
     * @param primaryStage the primary window of the JavaFX application
     */
    @Override
    public void start(Stage primaryStage) {
        // Create a File object representing the markdown file to display
        File markdownFile = new File("README.md");

        // Instantiate MarkdownWebView with the file and host services (to handle links)
        preview = new MarkdownWebView(markdownFile, getHostServices());

        
        	// Create a Scene containing the WebView, with initial size 800x600 pixels
		// The preview.launch() will return a WebView of the preview
		// If you want to reuse the same WebView later, just call getWebView()
		Scene scene = new Scene(preview.launch(), 800, 600);


        // Set the window title
        primaryStage.setTitle("Markdown in Scene");

        // Attach the scene to the primary stage (window)
        primaryStage.setScene(scene);

        // Show the window
        primaryStage.show();
    }

    /**
     * Called automatically when the application is stopping.
     * Disposes resources to clean up properly.
     */
    @Override
    public void stop() {
        // Check if preview exists and has not already been disposed
        if (preview != null && !preview.isDisposed()) {
            // Dispose the WebView and associated resources
            preview.dispose();
            // Terminate the JVM process cleanly
            System.exit(0);
        }
    }

    /**
     * Entry point when launching the application from the command line.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
