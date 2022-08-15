package co.elkin.veritran.model.account;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
public class Account {
    private Long id;
    private Long number;
    private BigDecimal balance;
    private BigDecimal debit;
    private BigDecimal credit;
    private String currency;
}
