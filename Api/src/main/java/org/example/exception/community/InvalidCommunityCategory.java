package org.example.exception.community;

public class InvalidCommunityCategory extends RuntimeException {
    public InvalidCommunityCategory(String message) {
        super(message);
    }
}
