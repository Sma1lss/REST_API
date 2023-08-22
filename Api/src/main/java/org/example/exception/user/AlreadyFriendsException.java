package org.example.exception.user;

public class AlreadyFriendsException extends RuntimeException {
    public AlreadyFriendsException(String message) {
        super(message);
    }
}
