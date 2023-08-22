package org.example.exception.user;

public class InvalidFriendRequestException extends RuntimeException {
    public InvalidFriendRequestException(String message) {
        super(message);
    }
}
