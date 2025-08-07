package itk.academy.wallet.dto;

import itk.academy.wallet.model.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationRequestDto {

    @NotNull(message = "must not be null")
    private UUID walletId;

    @NotNull(message = "must be DEPOSIT or WITHDRAW")
    private OperationType operationType;

    @NotNull(message = "must not be null")
    @Positive(message = "must be greater than zero")
    private BigDecimal amount;
}
