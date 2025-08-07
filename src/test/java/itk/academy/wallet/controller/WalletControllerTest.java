package itk.academy.wallet.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import itk.academy.wallet.dto.OperationRequestDto;
import itk.academy.wallet.dto.WalletResponseDto;
import itk.academy.wallet.dto.WalletShortResponseDto;
import itk.academy.wallet.model.OperationType;
import itk.academy.wallet.service.WalletService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getWallet() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletResponseDto walletDto = WalletResponseDto.builder()
                .walletId(walletId)
                .balance(new BigDecimal("100.0"))
                .build();
        when(walletService.getWallet(eq(walletId))).thenReturn(walletDto);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value("100.0"));
    }

    @Test
    void getAllWalletsDefaultParams() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletShortResponseDto walletDto = WalletShortResponseDto.builder()
                .walletId(walletId)
                .balance(new BigDecimal("200.0"))
                .build();
        Page<WalletShortResponseDto> page = new PageImpl<>(List.of(walletDto), PageRequest.of(0, 10), 1);
        when(walletService.getAllWallets(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.content[0].balance").value("200.0"))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getAllWalletsCustomParams() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletShortResponseDto walletDto = WalletShortResponseDto.builder()
                .walletId(walletId)
                .balance(new BigDecimal("300.0"))
                .build();
        Page<WalletShortResponseDto> page = new PageImpl<>(List.of(walletDto), PageRequest.of(1, 20), 1);
        when(walletService.getAllWallets(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/wallets")
                        .param("page", "1")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.content[0].balance").value("300.0"))
                .andExpect(jsonPath("$.pageable.pageNumber").value(1))
                .andExpect(jsonPath("$.pageable.pageSize").value(20));
    }

    @Test
    void getAllWalletsEmptyPage() throws Exception {
        Page<WalletShortResponseDto> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(walletService.getAllWallets(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(0));
    }


    @Test
    void createWallet() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletResponseDto walletDto = WalletResponseDto.builder()
                .walletId(walletId)
                .balance(new BigDecimal("0.0"))
                .build();
        when(walletService.createWallet()).thenReturn(walletDto);

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value("0.0"));
    }

    @Test
    void operateWallet() throws Exception {
        UUID walletId = UUID.randomUUID();
        OperationRequestDto requestDto = OperationRequestDto.builder()
                .walletId(walletId)
                .operationType(OperationType.DEPOSIT)
                .amount(new BigDecimal("100.0"))
                .build();
        WalletResponseDto responseDto = WalletResponseDto.builder()
                .walletId(walletId)
                .balance(new BigDecimal("100.0"))
                .build();
        when(walletService.makeOperation(any(OperationRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value("100.0"));
    }

    @Test
    void operateWalletInvalidRequest() throws Exception {
        OperationRequestDto requestDto = OperationRequestDto.builder()
                .walletId(null)
                .operationType(OperationType.DEPOSIT)
                .amount(new BigDecimal("100.0"))
                .build();

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}