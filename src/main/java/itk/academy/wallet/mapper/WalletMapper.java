package itk.academy.wallet.mapper;

import itk.academy.wallet.dto.OperationResponseDto;
import itk.academy.wallet.dto.WalletResponseDto;
import itk.academy.wallet.dto.WalletShortResponseDto;
import itk.academy.wallet.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WalletMapper {

    private final OperationMapper mapper;

    public WalletResponseDto toDto(Wallet entity) {
        List<OperationResponseDto> operations = Optional.ofNullable(entity.getOperationList())
                .orElse(Collections.emptyList())
                .stream()
                .map(mapper::toDto)
                .toList();

        return WalletResponseDto.builder()
                .walletId(entity.getWalletId())
                .balance(entity.getBalance())
                .operations(operations)
                .build();
    }

    public WalletShortResponseDto toShortDto(Wallet entity) {

        return WalletShortResponseDto.builder()
                .walletId(entity.getWalletId())
                .balance(entity.getBalance())
                .build();
    }
}
