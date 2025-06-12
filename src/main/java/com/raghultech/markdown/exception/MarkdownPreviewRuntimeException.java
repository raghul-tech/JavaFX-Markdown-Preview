package com.raghultech.markdown.exception;

@SuppressWarnings("serial")
public class MarkdownPreviewRuntimeException extends RuntimeException{

	 public MarkdownPreviewRuntimeException(String message) {
	        super(message);
	    }

	    public MarkdownPreviewRuntimeException(String message, Throwable cause) {
	        super(message, cause);
	    }
	
}
