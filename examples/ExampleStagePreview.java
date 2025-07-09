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

// Import the MarkdownWebView component you implemented
import io.github.raghultech.markdown.javafx.preview.MarkdownWebView;

// JavaFX imports
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;

/**
 * ExampleStagePreview demonstrates launching the Markdown preview
 * in a completely separate Stage window.
 */
public class ExampleStagePreview extends Application {

    // Holds the MarkdownWebView instance
    private MarkdownWebView preview;

    /**
     * This method is automatically called when the application starts.
     * It sets up the Stage and loads the markdown preview.
     */
    @Override
    public void start(Stage primaryStage) {
        // Create a File reference to the markdown file to preview
        File markdownFile = new File("README.md");

        // Initialize the MarkdownWebView with the file and host services
        preview = new MarkdownWebView(markdownFile, getHostServices());

        // Create a Scene containing the WebView (800x600 size)
        Scene scene = new Scene(preview.launch(), 800, 600);

        // Create a new Stage separate from the default primaryStage
        Stage markdownStage = new Stage();
        markdownStage.setTitle("Markdown in Separate Stage");
        markdownStage.setScene(scene);
        markdownStage.show();

        // Optional: hide the main (empty) primary stage
        primaryStage.hide();
    }

    /**
     * Called automatically when the application stops (e.g., window closed).
     * Ensures resources are disposed properly.
     */
    @Override
    public void stop() {
        if (preview != null && !preview.isDisposed()) {
            preview.dispose();
            System.exit(0);
        }
    }

    /**
     * Entry point: launches the JavaFX application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
