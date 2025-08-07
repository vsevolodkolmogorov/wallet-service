package itk.academy.wallet.exceptions;

import java.util.UUID;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(UUID id) {
        super("wallet with " + id + " not found");
    }
}
