package com.raghultech.markdown.exception;

@SuppressWarnings("serial")
public class MarkdownPreviewSystemException extends RuntimeException {
    public MarkdownPreviewSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public MarkdownPreviewSystemException(String message) {
        super(message);
    }
}