package itk.academy.wallet.controller;

import static org.junit.jupiter.api.Assertions.*;

import itk.academy.wallet.dto.OperationResponseDto;
import itk.academy.wallet.model.OperationType;
import itk.academy.wallet.service.OperationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OperationController.class)
class OperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperationService operationService;

    @Test
    void getAllOperationsDefaultParams() throws Exception {

        UUID operationId = UUID.randomUUID();
        OperationResponseDto operationDto = OperationResponseDto.builder()
                .operationId(operationId)
                .walletId(UUID.randomUUID())
                .operationType(OperationType.DEPOSIT)
                .amount(new BigDecimal("100.0"))
                .build();
        Page<OperationResponseDto> page = new PageImpl<>(List.of(operationDto), PageRequest.of(0, 10), 1);
        when(operationService.getAllOperations(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/operations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].operationId").value(operationId.toString()))
                .andExpect(jsonPath("$.content[0].operationType").value("DEPOSIT"))
                .andExpect(jsonPath("$.content[0].amount").value("100.0"))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getAllOperationsCustomParams() throws Exception {
        UUID operationId = UUID.randomUUID();
        OperationResponseDto operationDto = OperationResponseDto.builder()
                .operationId(operationId)
                .walletId(UUID.randomUUID())
                .operationType(OperationType.WITHDRAW)
                .amount(new BigDecimal("50.0"))
                .build();
        Page<OperationResponseDto> page = new PageImpl<>(List.of(operationDto), PageRequest.of(1, 20), 1);
        when(operationService.getAllOperations(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/operations")
                        .param("page", "1")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].operationId").value(operationId.toString()))
                .andExpect(jsonPath("$.content[0].operationType").value("WITHDRAW"))
                .andExpect(jsonPath("$.content[0].amount").value("50.0"))
                .andExpect(jsonPath("$.pageable.pageNumber").value(1))
                .andExpect(jsonPath("$.pageable.pageSize").value(20));
    }

    @Test
    void getAllOperationsEmptyPage() throws Exception {
        Page<OperationResponseDto> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(operationService.getAllOperations(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/operations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(0));
    }
}