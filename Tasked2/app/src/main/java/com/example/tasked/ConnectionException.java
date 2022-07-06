package com.example.tasked;

public class ConnectionException extends Exception{
    public ConnectionException() {
        super("There is an error with the connection");
    }
    public ConnectionException(String message) {
        super(message);
    }
}
