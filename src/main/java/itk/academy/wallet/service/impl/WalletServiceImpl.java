package itk.academy.wallet.service.impl;

import itk.academy.wallet.dto.OperationRequestDto;
import itk.academy.wallet.dto.WalletResponseDto;
import itk.academy.wallet.dto.WalletShortResponseDto;
import itk.academy.wallet.exceptions.WalletNotFoundException;
import itk.academy.wallet.mapper.WalletMapper;
import itk.academy.wallet.model.Wallet;
import itk.academy.wallet.repository.WalletRepository;
import itk.academy.wallet.service.OperationService;
import itk.academy.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final OperationService operationService;

    @Override
    public WalletResponseDto getWallet(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        WalletResponseDto walletDto = walletMapper.toDto(wallet);
        log.info("Wallet found: {}", walletDto.getWalletId());
        return walletDto;
    }

    @Override
    public Page<WalletShortResponseDto> getAllWallets(Pageable pageable) {
        Page<WalletShortResponseDto> pageWallets = walletRepository.findAll(pageable).map(walletMapper::toShortDto);
        log.info("Found wallets: {}", pageWallets.getTotalPages());
        return pageWallets;
    }

    @Override
    public WalletResponseDto createWallet() {
        Wallet wallet = walletRepository.save(Wallet.builder()
                .balance(new BigDecimal(0))
                .build());

        WalletResponseDto walletDto = walletMapper.toDto(wallet);
        log.info("Wallet created: {}", walletDto.getWalletId());
        return walletDto;
    }

    @Override
    @Transactional
    public WalletResponseDto makeOperation(OperationRequestDto operationDto) {
        Wallet wallet = walletRepository.findByIdForUpdate(operationDto.getWalletId())
                .orElseThrow(() -> new WalletNotFoundException(operationDto.getWalletId()));

        switch (Objects.requireNonNull(operationDto.getOperationType())) {
            case DEPOSIT -> wallet.deposit(operationDto.getAmount());
            case WITHDRAW -> wallet.withdraw(operationDto.getAmount());
            default -> throw new UnsupportedOperationException("Unsupported operation: " + operationDto.getOperationType());
        }

        operationService.createOperation(operationDto, wallet);

        Wallet updatedWallet = walletRepository.save(wallet);
        WalletResponseDto walletDto = walletMapper.toDto(updatedWallet);
        log.info("Operation completed: {}", walletDto.getWalletId());
        return walletDto;
    }
}
