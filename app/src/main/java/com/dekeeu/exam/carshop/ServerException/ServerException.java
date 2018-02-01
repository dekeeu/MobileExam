package com.dekeeu.exam.carshop.ServerException;

/**
 * Created by dekeeu on 31/01/2018.
 */

public class ServerException extends Exception {
    public ServerException() {
        super();
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
