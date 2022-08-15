package co.elkin.veritran.model.transactiontype;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class TransactionType {
    private Long id;
    private String name;
}
