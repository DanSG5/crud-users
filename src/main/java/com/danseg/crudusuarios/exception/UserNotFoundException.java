package com.danseg.crudusuarios.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message); // Llama al constructor de la superclase (RuntimeException) con el mensaje proporcionado
    }
}
