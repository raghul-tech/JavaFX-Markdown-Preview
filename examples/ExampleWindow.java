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
import java.util.Scanner;

import io.github.raghultech.markdown.javafx.preview.MarkdownWindow;
import javafx.application.Platform;

/**
 * ExampleWindow demonstrates how to launch a Markdown preview
 * in an independent window without a JavaFX Application subclass.
 *
 * It uses Platform.startup() to initialize JavaFX in a non-Application context.
 */
public class ExampleWindow {
    public static void main(String[] args) {
        // Initialize JavaFX platform (required when not using Application subclass)
        Platform.startup(() -> {
            // Disable automatic JVM exit when the last window closes
            Platform.setImplicitExit(false);
        });

        // The markdown file to preview
        File file = new File("README.md");

        // Create the MarkdownWindow
        MarkdownWindow preview = new MarkdownWindow(file);
        preview.setWindowName("Live Preview");
        preview.setWindowSize(900, 700);

        // Optionally set a custom icon
        // preview.setIcon("/justify.png");

        // Launch the preview window
        preview.launchPreview();

        // Use a Scanner to interact with the console
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Wait while the preview window is still open
            while (preview.isPreviewShowing()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }

            // When closed, prompt the user
            System.out.print("Preview closed. Reopen? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                // You can set dark mode before reopening
             //   preview.setDarkMode(true);

                // Optionally set a different icon
                // preview.setIcon("/MD.png");

                preview.reopenWindow();
            } else {
                break;
            }
        }

        scanner.close();
        System.exit(0);
    }
}
