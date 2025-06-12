package com.raghultech.markdown.exception;



@SuppressWarnings("serial")
public class MarkdownPreviewInitializationException extends RuntimeException {
    public MarkdownPreviewInitializationException(String message) {
        super(message);
    }

    public MarkdownPreviewInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
 