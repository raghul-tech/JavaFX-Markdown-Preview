package com.raghultech.markdown.exception;

@SuppressWarnings("serial")
public class MarkdownPreviewRenderException extends RuntimeException {
    public MarkdownPreviewRenderException(String message) {
        super(message);
    }

    public MarkdownPreviewRenderException(String message, Throwable cause) {
        super(message, cause);
    }
}

