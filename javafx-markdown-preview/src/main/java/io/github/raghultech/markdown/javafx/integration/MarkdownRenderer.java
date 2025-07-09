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
package io.github.raghultech.markdown.javafx.integration;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.gfm.users.GfmUsersExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import io.github.raghultech.markdown.javafx.config.JavaFXTheme;

public class MarkdownRenderer {

	
	public synchronized String renderMarkdown(String markdown) {
	    MutableDataSet options = new MutableDataSet();
	    options.set(Parser.EXTENSIONS, Arrays.asList(
	        TablesExtension.create(),
	        TaskListExtension.create(),
	        StrikethroughExtension.create(),
	        GfmUsersExtension.create()
	    ));
	    options.set(HtmlRenderer.SOFT_BREAK, "<br />");

	    Parser parser = Parser.builder(options).build();
	    HtmlRenderer renderer = HtmlRenderer.builder(options).build();

	    String html = renderer.render(parser.parse(markdown));

	    // Important: Process emojis only in non-code segments
	//    return processAllEmojis(html);
	 // Important: Process emojis only in non-code segments
	    html = processAllEmojis(html);

	    // Inject copy buttons into code blocks
	    html = injectCopyButtons(html);

	    return html;
	}
	/**
	 * Wraps each <pre><code>...</code></pre> in a div with a copy button.
	 */
	private String injectCopyButtons(String html) {
	    return html.replaceAll(
	        "(?s)<pre><code class=\"([^\"]+)\">(.*?)</code></pre>",
	        "<div class=\"code-block\">" +
	            "<button class=\"copy-button\" title=\"Copy to clipboard\">" +
	            "<svg class=\"icon-copy\" xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" viewBox=\"0 0 16 16\">" +
	            "<path d=\"M10 1H2a1 1 0 0 0-1 1v11h1V2h8V1zm3 2H5a1 1 0 0 0-1 1v11a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V4a1 1 0 0 0-1-1zm0 12H5V4h8v11z\"/>" +
	            "</svg>" +
	            "<svg class=\"icon-check\" style=\"display:none\" xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"green\" viewBox=\"0 0 16 16\">" +
	            "<path d=\"M13.485 1.929a1 1 0 0 1 0 1.414L6.414 10.414a1 1 0 0 1-1.414 0L2.515 7.929a1 1 0 1 1 1.414-1.414L6 8.586l6.071-6.071a1 1 0 0 1 1.414 0z\"/>" +
	            "</svg>" +
	            "</button>" +
	            "<pre><code class=\"$1\">$2</code></pre></div>"
	    );
	}

	/**
	 * Replaces emojis with <img> fallback HTML,
	 * but **skips any emojis inside <code> or <pre> blocks**
	 */
	private synchronized String processAllEmojis(String html) {
	    // Regex to match all <code>...</code> or <pre>...</pre> segments
	    Pattern codePattern = Pattern.compile("(?s)(<pre.*?>.*?</pre>|<code.*?>.*?</code>)");
	    Matcher matcher = codePattern.matcher(html);

	    int lastEnd = 0;
	    StringBuilder result = new StringBuilder();

	    while (matcher.find()) {
	        // Text before the <code>/<pre> block
	        String before = html.substring(lastEnd, matcher.start());
	        result.append(replaceEmojisInSegment(before));

	        // Append <code>/<pre> block untouched
	        result.append(matcher.group(1));

	        lastEnd = matcher.end();
	    }

	    // Append the tail after the last <code>/<pre> block
	    result.append(replaceEmojisInSegment(html.substring(lastEnd)));

	    return result.toString();
	}

	/**
	 * Processes a chunk of HTML (not inside code) and replaces emojis
	 */
	private String replaceEmojisInSegment(String segment) {
	    // The emoji regex as in your code
	    Pattern emojiPattern = Pattern.compile(
	        "[\u00A9\u00AE\u203C\u2049\u2122\u2139\u2194-\u2199\u21A9\u21AA\u231A\u231B\u2328\u23CF" +
	        "\u23E9-\u23F3\u23F8-\u23FA\u24C2\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE\u2600-\u2604\u260E" +
	        "\u2611\u2614\u2615\u2618\u261D\u2620\u2622\u2623\u2626\u262A\u262E\u262F\u2638-\u263A\u2640" +
	        "\u2642\u2648-\u2653\u265F\u2660\u2663\u2665\u2666\u2668\u267B\u267E\u267F\u2692-\u2697\u2699" +
	        "\u269B\u269C\u26A0\u26A1\u26AA\u26AB\u26B0\u26B1\u26BD\u26BE\u26C4\u26C5\u26C8\u26CE\u26CF" +
	        "\u26D1\u26D3\u26D4\u26E9\u26EA\u26F0-\u26F5\u26F7-\u26FA\u26FD\u2702\u2705\u2708-\u270D" +
	        "\u270F\u2712\u2714\u2716\u271D\u2721\u2728\u2733\u2734\u2744\u2747\u274C\u274E\u2753-\u2755" +
	        "\u2757\u2763\u2764\u2795-\u2797\u27A1\u27B0\u27BF\u2934\u2935\u2B05-\u2B07\u2B1B\u2B1C\u2B50" +
	        "\u2B55\u3030\u303D\u3297\u3299" +
	        "\uD83C\uDC04\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD92-\uD83C\uDD9A" +
	        "\uD83C\uDDE6-\uD83C\uDDFF\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A" +
	        "\uD83C\uDE50\uD83C\uDE51" +
	        "\uD83C\uDF00-\uD83C\uDFFF" +
	        "\uD83D\uDC00-\uD83D\uDDFF" +
	        "\uD83D\uDE00-\uD83D\uDE4F\uD83D\uDE80-\uD83D\uDEFF" +
	        "\uD83E\uDD00-\uD83E\uDDFF" +
	        "\uD83E\uDE70-\uD83E\uDE74\uD83E\uDE78-\uD83E\uDE7A\uD83E\uDE80-\uD83E\uDE86" +
	        "\uD83E\uDE90-\uD83E\uDE95\uD83E\uDEA0-\uD83E\uDEA5\uD83E\uDEB0-\uD83E\uDEB6" +
	        "\uD83E\uDEB7-\uD83E\uDEB8\uD83E\uDEB9-\uD83E\uDEBC\uD83E\uDEBD-\uD83E\uDEC0" +
	        "\uD83E\uDE9F" +
	        "\uD83E\uDE00-\uD83E\uDEFF" +
	        "]",
	        Pattern.UNICODE_CHARACTER_CLASS
	    );

	    Matcher matcher = emojiPattern.matcher(segment);
	    StringBuffer sb = new StringBuffer();
	    while (matcher.find()) {
	        String emoji = matcher.group();
	        String code = toCodePoint(emoji);
	        String replacement = String.format(
	            "<span class=\"emoji-container\">" +
	            "<img class=\"emoji\" alt=\"%s\" src=\"https://github.githubassets.com/images/icons/emoji/unicode/%s.png\" " +
	            "onerror=\"this.style.display='none'; this.nextElementSibling.style.display='inline'\">" +
	            "<span class=\"emoji-fallback\" style=\"display:none\">%s</span>" +
	            "</span>",
	            emoji, code, emoji
	        );
	        matcher.appendReplacement(sb, replacement);
	    }
	    matcher.appendTail(sb);
	    return sb.toString();
	}

	/**
	 * Converts a String emoji to its hex codepoint(s)
	 */
	private  String toCodePoint(String emoji) {
	    if (emoji.codePointCount(0, emoji.length()) == 1) {
	        return Integer.toHexString(emoji.codePointAt(0));
	    }
	    StringBuilder codePoints = new StringBuilder();
	    int offset = 0;
	    while (offset < emoji.length()) {
	        int codePoint = emoji.codePointAt(offset);
	        codePoints.append(Integer.toHexString(codePoint));
	        offset += Character.charCount(codePoint);
	        if (offset < emoji.length()) {
	            codePoints.append("-");
	        }
	    }
	    return codePoints.toString();
	}

	public synchronized String getStyledHtml(String content, boolean isDark) {
		return getStyledHtml(content,null,isDark);
	}
	
	
	public synchronized String getStyledHtml(String content, File mdFile, boolean isDark) {
	    if (content == null) {
	        throw new IllegalArgumentException("Markdown content cannot be null.");
	    }

	    String basePath = getBasePathForFile(mdFile);
	    if (basePath == null) {
			basePath = "";
		}

	    // Pick your CSS from Theme
	    String themeCss = isDark ? JavaFXTheme.DARK_CSS : JavaFXTheme.LIGHT_CSS;

	    // Highlight.js theme
	    String highlightCssUrl = isDark
	        ? "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/github-dark.min.css"
	        : "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/github.min.css";

	    String script = """
	        document.addEventListener('DOMContentLoaded', function() {
	            function copyCode(button) {
	                const codeBlock = button.closest('.code-block');
	                const codeElement = codeBlock.querySelector('code');
	                const text = codeElement.textContent || '';

	                const textArea = document.createElement('textarea');
	                textArea.value = text;
	                textArea.style.position = 'fixed';
	                textArea.style.opacity = 0;
	                document.body.appendChild(textArea);
	                textArea.select();

	                try {
	                    const successful = document.execCommand('copy');
	                    if (successful) {
	                        const iconCopy = button.querySelector('.icon-copy');
	                        const iconCheck = button.querySelector('.icon-check');
	                        iconCopy.style.display = 'none';
	                        iconCheck.style.display = 'inline';
	                        codeBlock.classList.add('copied');
	                        setTimeout(() => {
	                            iconCopy.style.display = 'inline';
	                            iconCheck.style.display = 'none';
	                            codeBlock.classList.remove('copied');
	                        }, 1500);
	                    }
	                } finally {
	                    document.body.removeChild(textArea);
	                }
	            }
	            document.body.addEventListener('click', function(e) {
	                const button = e.target.closest('.copy-button');
	                if (button) {
	                    copyCode(button);
	                }
	            });
	        });
	        """;

	    return String.format("""
	        <!DOCTYPE html>
	        <html>
	          <head>
	            <meta charset="UTF-8">
	            <base href="%s">
	            <style>%s</style>
	            <link rel="stylesheet" href="%s">
	            <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/highlight.min.js"></script>
	            <script>hljs.highlightAll();</script>
	          </head>
	          <body>%s
	            <script>%s</script>
	          </body>
	        </html>
	        """,
	        basePath,
	        themeCss,
	        highlightCssUrl,
	        content,
	        script
	    );
	}    
	    

	    private  String getBasePathForFile(File mdFile) {
	        if (mdFile != null && mdFile.getParentFile() != null) {
	            try {
	                return mdFile.getParentFile().toURI().toString();
	            } catch (Exception e) {
	            //    System.err.println("Error creating base path URI: " + e.getMessage());
	            }
	        }
	        return null;
	    }
	
	
}
