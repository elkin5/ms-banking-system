package co.elkin.veritran.model.transfer.gateways;

import co.elkin.veritran.model.transfer.Transfer;
import reactor.core.publisher.Mono;

public interface TransferRepository {
    Mono<Transfer> save(Transfer transfer);
}
