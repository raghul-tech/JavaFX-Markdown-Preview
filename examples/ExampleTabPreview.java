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
 * the MarkdownWebView in a TabPane.
 *
 * <p>This allows you to show the preview inside a tab,
 * optionally alongside other tabs in your UI.</p>
 *
 * Usage:
 * - Run this class as a JavaFX application.
 * - The README.md file will be rendered inside a tab.
 */
import io.github.raghultech.markdown.javafx.preview.MarkdownWebView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import java.io.File;

public class ExampleTabPreview extends Application {

    /** The Markdown preview component instance. */
    private MarkdownWebView preview;

    @Override
    public void start(Stage primaryStage) {
        // Create a reference to the markdown file you want to display
        File markdownFile = new File("README.md");

        // Initialize MarkdownWebView with the file and host services
        preview = new MarkdownWebView(markdownFile, getHostServices());

        // Create a Tab that contains the preview WebView
        Tab markdownTab = new Tab("Markdown Preview", preview.launch());

        // Dispose resources when the tab is closed
        markdownTab.setOnClosed(e -> preview.dispose());

        // Create a TabPane and add the markdown tab
        TabPane tabPane = new TabPane(markdownTab);

        // Create a Scene with the TabPane, specifying dimensions
        Scene scene = new Scene(tabPane, 900, 700);

        // Configure and show the primary stage
        primaryStage.setTitle("Markdown in Tab");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Dispose preview resources when application exits
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
