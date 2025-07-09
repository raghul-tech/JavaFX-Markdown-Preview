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

import java.io.File;

import io.github.raghultech.markdown.javafx.preview.MarkdownWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * ExampleWindow2 demonstrates how to show a Markdown preview
 * using a standalone window (MarkdownWindow) inside a standard JavaFX application.
 *
 * It simplifies usage for users already extending JavaFX Application.
 */
public class ExampleWindow2 extends Application {

    /**
     * Entry point of the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);  // Launches the JavaFX application
    }

    /**
     * Called by the JavaFX runtime when the application starts.
     *
     * @param primaryStage the main stage (not used in this example)
     */
    @Override
    public void start(Stage primaryStage) {
        File file = new File("README.md");

        // Show the Markdown preview window for the file
        showMarkdownPreview(file);
    }

    /**
     * Utility method to show a Markdown preview window with default settings.
     *
     * @param file the markdown file to preview
     */
    public static void showMarkdownPreview(File file) {
        MarkdownWindow preview = new MarkdownWindow(file);
        preview.setWindowName("Live Preview");
        preview.setWindowSize(900, 700);
        
        // Optionally set dark mode, icon, etc.
        // preview.setDarkMode(true);
        // preview.setIcon("/path/to/icon.png");

        preview.launchPreview();  // Launch the preview window
    }
}
