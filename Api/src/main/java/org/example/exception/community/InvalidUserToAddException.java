package org.example.exception.community;

public class InvalidUserToAddException extends RuntimeException {
    public InvalidUserToAddException(String message) {
        super(message);
    }
}
