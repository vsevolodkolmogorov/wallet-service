package itk.academy.wallet.controller;

import itk.academy.wallet.dto.OperationResponseDto;
import itk.academy.wallet.service.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Slf4j
public class OperationController {

    private final OperationService operationService;

    @GetMapping("/operations")
    public ResponseEntity<Page<OperationResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Received request to fetch all operations, page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(operationService.getAllOperations(pageable));
    }
}
