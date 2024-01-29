package br.com.alura.fipe.exceptions;

import java.io.IOException;

public class ExceptionHandler {
    public static void treatException(String message, Throwable e) {
        System.out.println(message + ": " + e.getMessage());
        //e.printStackTrace();
    }
}
