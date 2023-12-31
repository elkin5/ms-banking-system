package co.elkin.veritran.model.client;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Client {
    private String name;
    private String email;
    private String lastName;
    private LocalDate birthDate;
    private String documentNumber;
}
