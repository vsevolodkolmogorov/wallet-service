package itk.academy.wallet.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import itk.academy.wallet.exceptions.BalanceToLowException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "operationList")
public class Wallet {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID walletId;

    private BigDecimal balance;

    @Version
    private Long version;

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) {
            throw new BalanceToLowException(amount, this.balance);
        }
        this.balance = this.balance.subtract(amount);
    }

    @OneToMany(mappedBy = "wallet", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Operation> operationList = new ArrayList<>();
}
