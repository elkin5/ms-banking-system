package co.elkin.veritran.model.transfer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Transfer {
    private Long id;
    private Long originTransactionId;
    private Long destinationTransactionId;
}
