package com.raghultech.markdown.exception;

@SuppressWarnings("serial")
public class MarkdownPreviewContentException extends RuntimeException {
    public MarkdownPreviewContentException(String message) {
        super(message);
    }

    public MarkdownPreviewContentException(String message, Throwable cause) {
        super(message, cause);
    }
}
