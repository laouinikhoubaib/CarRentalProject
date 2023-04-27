package com.example.carrental.Exceptions;



public class AgenceNotFoundException extends RuntimeException {

    public AgenceNotFoundException(String message) {
        super(message);
    }
}
