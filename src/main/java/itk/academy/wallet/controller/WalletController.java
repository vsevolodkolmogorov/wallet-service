package itk.academy.wallet.controller;

import itk.academy.wallet.dto.OperationRequestDto;
import itk.academy.wallet.dto.WalletResponseDto;
import itk.academy.wallet.dto.WalletShortResponseDto;
import itk.academy.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<WalletResponseDto> getWallet(@PathVariable UUID walletId) {
        log.info("Received request to fetch wallet by id: {}", walletId);

        WalletResponseDto wallet = walletService.getWallet(walletId);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/wallets")
    public ResponseEntity<Page<WalletShortResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Received request to fetch all wallets, page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(walletService.getAllWallets(pageable));
    }

    @PostMapping("/wallets")
    public ResponseEntity<WalletResponseDto> createWallet() {
        log.info("Received request to create wallet from scratch");

        WalletResponseDto wallet = walletService.createWallet();
        return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    @PostMapping("/wallet")
    public ResponseEntity<WalletResponseDto> operateWallet(@Validated @RequestBody OperationRequestDto request) {
        log.info("Received request {} , to make operation with wallet", request);

        WalletResponseDto updatedWallet = walletService.makeOperation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedWallet);
    }
}
