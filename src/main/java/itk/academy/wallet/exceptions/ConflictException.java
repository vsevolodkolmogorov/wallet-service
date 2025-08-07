package itk.academy.wallet.exceptions;

import java.math.BigDecimal;

public class ConflictException extends RuntimeException {

    public ConflictException(String text) {
        super(text);
    }
}
