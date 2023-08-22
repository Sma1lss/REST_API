package org.example.exception.community;

public class InvalidCommunityDescriptionException extends RuntimeException {
    public InvalidCommunityDescriptionException(String message) {
        super(message);
    }
}
