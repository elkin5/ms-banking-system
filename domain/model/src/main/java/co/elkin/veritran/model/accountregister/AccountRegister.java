package co.elkin.veritran.model.accountregister;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AccountRegister {
    private Long id;
    private Long accountId;
    private Long clientId;
    private String status;
}
