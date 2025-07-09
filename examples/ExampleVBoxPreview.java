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
 * Example JavaFX application demonstrating how to embed
 * MarkdownWebView inside a VBox layout, along with a button
 * to manually dispose the preview when desired.
 *
 * <p>This example shows how to integrate the Markdown preview
 * into more complex layouts (e.g., side panels or toolbars).
 */
import io.github.raghultech.markdown.javafx.preview.MarkdownWebView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;

public class ExampleVBoxPreview extends Application {

    /** The Markdown preview component instance. */
    private MarkdownWebView preview;

    @Override
    public void start(Stage primaryStage) {
        // Reference to the markdown file to display
        File markdownFile = new File("README.md");

        // Initialize the MarkdownWebView
        preview = new MarkdownWebView(markdownFile, getHostServices());

        // Create a VBox layout containing the MarkdownWebView
        VBox vbox = new VBox(preview.launch());

        // Add a button below the preview to manually dispose it
        Button disposeButton = new Button("Dispose Preview");
        disposeButton.setOnAction(e -> preview.dispose());

        // Add the button to the VBox
        vbox.getChildren().add(disposeButton);

        // Create a Scene containing the VBox
        Scene scene = new Scene(vbox, 800, 600);

        // Set up and show the main window
        primaryStage.setTitle("Markdown in VBox");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Ensure resources are disposed when the application exits
        if (preview != null && !preview.isDisposed()) {
            preview.dispose();
            system.exit(0);
        }
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
