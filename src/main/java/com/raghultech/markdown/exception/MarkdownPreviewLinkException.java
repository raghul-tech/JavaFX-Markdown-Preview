package com.raghultech.markdown.exception;

@SuppressWarnings("serial")
public class MarkdownPreviewLinkException extends RuntimeException {
    public MarkdownPreviewLinkException(String message, Throwable cause) {
        super(message, cause);
    }
    public MarkdownPreviewLinkException(String message) {
        super(message);
    }
}
