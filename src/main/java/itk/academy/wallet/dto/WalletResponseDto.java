package itk.academy.wallet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletResponseDto {

    @NotNull
    private UUID walletId;

    @NotNull
    @Positive
    private BigDecimal balance;

    private List<OperationResponseDto> operations;
}
