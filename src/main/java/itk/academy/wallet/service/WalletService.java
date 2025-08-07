package itk.academy.wallet.service;

import itk.academy.wallet.dto.OperationRequestDto;
import itk.academy.wallet.dto.WalletResponseDto;
import itk.academy.wallet.dto.WalletShortResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface WalletService {
    WalletResponseDto getWallet(UUID walletId);
    Page<WalletShortResponseDto> getAllWallets(Pageable pageable);
    WalletResponseDto createWallet();
    WalletResponseDto makeOperation(OperationRequestDto wallet);
}
