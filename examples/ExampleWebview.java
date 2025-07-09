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

/**
 * Example JavaFX application showing multiple ways to create a MarkdownWebView:
 * - From a Markdown file.
 * - From a Markdown string.
 * - From an existing WebView (commented out here).
 *
 * <p>This example is useful for demonstrating different initialization patterns
 * depending on your application workflow.
 */
import java.io.File;

import io.github.raghultech.markdown.javafx.preview.MarkdownWebView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
// import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class ExampleWebview extends Application {

    /** The Markdown preview component instance. */
    private MarkdownWebView preview;

    @Override
    public void start(Stage primaryStage) {
        // Example: Load preview content from a markdown file
        File markdownFile = new File("README.md");

        // OR Example: Load preview content from a Markdown string
        // String markdownContent = "# Hello Markdown!\nThis is **bold** text.";

        // Initialize the MarkdownWebView
        preview = new MarkdownWebView(markdownFile, getHostServices());

        // If you want to use a string instead of a file:
        // preview = new MarkdownWebView(markdownContent, getHostServices());

        // If you want to attach to an existing WebView instance:
        // WebView view = new WebView();
        // preview = new MarkdownWebView(view, markdownFile, getHostServices());

        // Create a button to dispose the preview manually
        Button disposeButton = new Button("Dispose Preview");
        disposeButton.setOnAction(e -> preview.dispose());

        // Create a VBox layout containing the preview and button
        VBox root = new VBox(preview.launch(), disposeButton);

        // Create a Scene with the VBox
        Scene scene = new Scene(root, 800, 600);

        // Configure and show the primary stage
        primaryStage.setTitle("Markdown Preview Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Always dispose resources when application exits
        if (preview != null && !preview.isDisposed()) {
            preview.dispose();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
