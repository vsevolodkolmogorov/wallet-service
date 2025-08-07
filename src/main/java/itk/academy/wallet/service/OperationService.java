package itk.academy.wallet.service;

import itk.academy.wallet.dto.OperationRequestDto;
import itk.academy.wallet.dto.OperationResponseDto;
import itk.academy.wallet.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OperationService {
    void createOperation(OperationRequestDto operationDto, Wallet wallet);
    Page<OperationResponseDto> getAllOperations(Pageable pageable);
}
