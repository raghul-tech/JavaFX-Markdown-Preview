import com.raghultech.markdown.preview.JavaFXMarkdownPreview;

import javax.swing.*;
import java.io.File;
import java.util.*;

/**
 * Example to preview a markdown (.md) file using JavaFXMarkdownPreview.
 */

public class Markdown1 {

   public static void main(String[] args) {
        File file = new File("demo.md");
          JavaFXMarkdownPreview preview = new JavaFXMarkdownPreview(file);
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
