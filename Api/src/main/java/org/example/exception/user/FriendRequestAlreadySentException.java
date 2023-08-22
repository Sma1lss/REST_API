package org.example.exception.user;

public class FriendRequestAlreadySentException extends RuntimeException {
    public FriendRequestAlreadySentException(String message) {
        super(message);
    }
}
