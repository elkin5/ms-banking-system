package co.elkin.veritran.model.transfer;

import co.elkin.veritran.model.transaction.Transaction;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Transfer {
    Transaction deposit;
    Transaction withdraw;
}
