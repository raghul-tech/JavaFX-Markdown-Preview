package com.raghultech.markdown.exception;

@SuppressWarnings("serial")
public class MarkdownPreviewWindowException extends RuntimeException {
    public MarkdownPreviewWindowException(String message, Throwable cause) {
        super(message, cause);
    }
    public MarkdownPreviewWindowException(String message) {
        super(message);
    }
}
