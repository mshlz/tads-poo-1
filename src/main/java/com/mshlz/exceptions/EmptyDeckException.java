package com.mshlz.exceptions;

public class EmptyDeckException extends Throwable {
    public EmptyDeckException() {
        super("Empty deck");
    }
}
