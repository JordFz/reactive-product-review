package jfc.microservices.api.exceptions;

import jfc.microservices.api.core.product.Product;

public class InvalidInputExceptions extends RuntimeException{
    public InvalidInputExceptions() {
    }

    public InvalidInputExceptions(String message) {
        super(message);
    }

    public InvalidInputExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputExceptions(Throwable cause) {
        super(cause);
    }

}
