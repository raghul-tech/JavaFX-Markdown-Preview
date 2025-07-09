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
 * ExampleRenderer demonstrates rendering Markdown files
 * into styled HTML using the MarkdownRenderer utility.
 *
 * <p>
 * This example:
 * <ul>
 *   <li>Loads a Markdown file</li>
 *   <li>Converts it to HTML</li>
 *   <li>Applies styling for dark theme</li>
 *   <li>Prints the styled HTML output</li>
 * </ul>
 */
import java.io.File;

import io.github.raghultech.markdown.javafx.integration.MarkdownRenderer;
import io.github.raghultech.markdown.utils.openloom.OpenLoom;

public class ExampleRenderer {

    /**
     * The main method is the entry point of this example program.
     *
     * @param args Command-line arguments (not used here).
     */
    public static void main(String[] args) {

        // Create a File object representing your Markdown document
        File mdFile = new File("README.md");

        // Create a MarkdownRenderer instance
        MarkdownRenderer renderer = new MarkdownRenderer();

        // Use OpenLoom to safely read file content into a String
        String content = OpenLoom.getContent(mdFile).toString();

        // Convert Markdown to basic HTML
        String html = renderer.renderMarkdown(content);

        // Convert the HTML into a styled HTML page
        // You can also pass null instead of mdFile if you are rendering content
        // from user input or an API (i.e., no file context for relative links)
        String styledHtml = renderer.getStyledHtml(
            html,
            mdFile, // Optional: helps resolve relative paths
            true    // true = Dark theme, false = Light theme
        );

        // Print the styled HTML to console
        System.out.println(styledHtml);
    }
}
