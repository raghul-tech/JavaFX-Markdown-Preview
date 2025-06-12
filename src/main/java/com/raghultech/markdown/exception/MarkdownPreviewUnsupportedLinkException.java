package com.raghultech.markdown.exception;

@SuppressWarnings("serial")
public class MarkdownPreviewUnsupportedLinkException extends RuntimeException {
    public MarkdownPreviewUnsupportedLinkException(String message) {
        super(message);
    }
}
