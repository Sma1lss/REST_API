package org.example.exception.community;

public class InvalidCommunityNameException extends RuntimeException {
    public InvalidCommunityNameException(String message) {
        super(message);
    }
}
