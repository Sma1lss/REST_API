package org.example.exception.user;

public class InvalidFriendshipException extends RuntimeException {
    public InvalidFriendshipException(String message) {
        super(message);
    }
}
