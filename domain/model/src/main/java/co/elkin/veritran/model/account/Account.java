package co.elkin.veritran.model.account;

import co.elkin.veritran.model.Amount;
import co.elkin.veritran.model.client.Client;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Account {
    private Long number;
    private Amount debit;
    private Amount credit;
    private String currency;
    private String status;
    private Client client;

    public Amount getBalance() {
        return new Amount(this.credit.getValue().subtract(this.debit.getValue()));
    }
}
