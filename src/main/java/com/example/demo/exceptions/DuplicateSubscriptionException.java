package com.example.demo.exceptions;

public class DuplicateSubscriptionException extends RuntimeException{
    public DuplicateSubscriptionException(String error) {
        super(error);
    }
}
