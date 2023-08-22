package org.example.exception.post;

public class UserHasNotLikedPostException extends RuntimeException {
    public UserHasNotLikedPostException(String message) {
        super(message);
    }
}
