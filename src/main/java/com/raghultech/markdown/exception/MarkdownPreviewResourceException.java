package com.raghultech.markdown.exception;

@SuppressWarnings("serial")
public class MarkdownPreviewResourceException extends RuntimeException{

	 public MarkdownPreviewResourceException(String message) {
	        super(message);
	    }

	    public MarkdownPreviewResourceException(String message, Throwable cause) {
	        super(message, cause);
	    }
	
}
