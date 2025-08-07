package itk.academy.wallet.exceptions;

import java.math.BigDecimal;

public class BalanceToLowException extends RuntimeException {

    public BalanceToLowException(BigDecimal amount, BigDecimal balance) {
        super("balance " + balance + " not enough funds to withdraw " + amount);
    }
}
