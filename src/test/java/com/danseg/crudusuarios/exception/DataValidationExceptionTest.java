package com.danseg.crudusuarios.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataValidationExceptionTest {

    @Test
    public void dataValidationExceptionTest() {
        String errorMessage = "Error de validación de datos";

        DataValidationException exception = new DataValidationException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}

