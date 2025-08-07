package itk.academy.wallet.service.impl;

import itk.academy.wallet.dto.OperationRequestDto;
import itk.academy.wallet.dto.OperationResponseDto;
import itk.academy.wallet.mapper.OperationMapper;
import itk.academy.wallet.model.Operation;
import itk.academy.wallet.model.Wallet;
import itk.academy.wallet.repository.OperationRepository;
import itk.academy.wallet.service.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;

    @Override
    @Async
    public void createOperation(OperationRequestDto operationDto, Wallet wallet) {
        Operation operation = operationMapper.toEntity(operationDto, wallet);
        operationRepository.save(operation);
        log.info("Operation {} was created asynchronously", operation.getOperationId());
    }

    @Override
    public Page<OperationResponseDto> getAllOperations(Pageable pageable) {
        Page<OperationResponseDto> allOperations = operationRepository.findAll(pageable).map(operationMapper::toDto);
        log.info("Found operations: {}", allOperations.getTotalPages());
        return allOperations;
    }
}
