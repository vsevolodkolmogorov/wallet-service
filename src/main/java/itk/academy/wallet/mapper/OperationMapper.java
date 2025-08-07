package itk.academy.wallet.mapper;

import itk.academy.wallet.dto.OperationRequestDto;
import itk.academy.wallet.dto.OperationResponseDto;
import itk.academy.wallet.model.Operation;
import itk.academy.wallet.model.Wallet;
import org.springframework.stereotype.Component;

@Component
public class OperationMapper {

    public Operation toEntity(OperationRequestDto dto, Wallet wallet) {
        return Operation.builder()
                .amount(dto.getAmount())
                .operationType(dto.getOperationType())
                .wallet(wallet)
                .build();
    }

    public OperationResponseDto toDto(Operation entity) {
        return OperationResponseDto.builder()
                .operationId(entity.getOperationId())
                .operationType(entity.getOperationType())
                .amount(entity.getAmount())
                .walletId(entity.getWallet().getWalletId())
                .build();
    }
}
