package co.elkin.veritran.model.transaction;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Transaction {
    private Long id;
    private Long transactionTypeId;
    private Long accountId;
    private Long clientId;
    private BigDecimal amount;
    private LocalDateTime creationDate;
}
