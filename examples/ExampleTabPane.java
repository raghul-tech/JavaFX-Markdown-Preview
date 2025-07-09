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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import io.github.raghultech.markdown.javafx.preview.MarkdownTab;

/**
 * ExampleTabPane demonstrates how to integrate MarkdownTab into a JavaFX TabPane.
 * It shows how to open Markdown files dynamically and preview them in separate tabs.
 */
public class ExampleTabPane extends Application {

    /**
     * Application entry point when launched from the command line.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Called by JavaFX when the application starts.
     *
     * @param primaryStage the main window
     */
    @Override
    public void start(Stage primaryStage) {
        // Create the main TabPane that holds all tabs
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        // Add some initial tabs containing text editors
        tabPane.getTabs().addAll(
            createEditorTab("Editor 1"),
            createEditorTab("Editor 2")
        );

        // Create the root BorderPane and embed the tab pane
        BorderPane root = new BorderPane(tabPane);
        Scene scene = new Scene(root, 1200, 900);

        // Add a MenuBar with a File -> Open Markdown option
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open Markdown");
        openItem.setOnAction(e -> openMarkdownFile(tabPane));
        fileMenu.getItems().add(openItem);
        menuBar.getMenus().add(fileMenu);
        root.setTop(menuBar);

        // Set up the main window
        primaryStage.setTitle("Markdown Preview Tabs (JavaFX)");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Example: Automatically open a markdown file on startup
        File markdownFile = new File("README.md");
        if (markdownFile.exists()) {
            createMarkdownPreviewTab(tabPane, markdownFile);
        }
    }

    /**
     * Creates a Tab containing a TextArea editor.
     *
     * @param title the title of the tab
     * @return the Tab with an editable TextArea
     */
    private Tab createEditorTab(String title) {
        Tab tab = new Tab(title);
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        tab.setContent(new ScrollPane(textArea));
        return tab;
    }

    /**
     * Opens a file chooser to let the user select a Markdown file,
     * then creates a preview tab for it.
     *
     * @param tabPane the TabPane to which the new tab will be added
     */
    private void openMarkdownFile(TabPane tabPane) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Markdown File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Markdown Files", "*.md", "*.markdown"),
            new FileChooser.ExtensionFilter("Text Files", "*.txt"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File file = fileChooser.showOpenDialog(tabPane.getScene().getWindow());
        if (file != null) {
            createMarkdownPreviewTab(tabPane, file);
        }
    }

    /**
     * Creates a new tab with a Markdown preview using MarkdownTab.
     *
     * @param tabPane the TabPane to which the preview tab is added
     * @param file    the Markdown file to render
     */
    private void createMarkdownPreviewTab(TabPane tabPane, File file) {
        // Create a new Tab to hold the preview
        Tab previewTab = new Tab("Preview: " + file.getName());
        tabPane.getTabs().add(previewTab);

        // Initialize MarkdownTab
        MarkdownTab preview = new MarkdownTab(previewTab, file);
        preview.setHostServices(getHostServices());
        preview.setTabName("Preview: " + file.getName());
       // preview.setDarkMode(true); // Enable dark theme
        preview.launchTab();

        // Select the preview tab
        tabPane.getSelectionModel().select(previewTab);
    }
}
