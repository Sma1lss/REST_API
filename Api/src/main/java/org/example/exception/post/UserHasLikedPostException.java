package org.example.exception.post;

public class UserHasLikedPostException extends RuntimeException {
    public UserHasLikedPostException(String message) {
        super(message);
    }
}
