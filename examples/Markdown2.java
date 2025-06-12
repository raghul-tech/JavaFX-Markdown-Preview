import com.raghultech.markdown.preview.JavaFXMarkdownPreview;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class Markdown2 {

   public static void main(String[] args) {
  
  String content = """
                # ☕ JavaFX Markdown Preview

                This is a simple and elegant **Markdown preview tool**.

                ## ✅ Key Features
                - Live preview
                - Load from string or `.md` file
                - Lightweight JavaFX integration

                ---

                **Enjoy building beautiful JavaFX apps!**
                """;
    
JavaFXMarkdownPreview preview = new JavaFXMarkdownPreview(content);
        preview.launchPreview();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            while (preview.isPreviewShowing()) {
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }

            System.out.print("Preview window closed. Reopen? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                preview.reopenWindow();
            } else {
                System.out.println("Exiting...");
               break;
                
            }
        }

        scanner.close();
        System.exit(0);
    }
}
