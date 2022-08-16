package co.elkin.veritran.usecase.client;

import co.elkin.veritran.model.client.Client;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindClientUseCase {
    public Mono<Client> findByDocumentNumber(String documentNumber){
        return null;
    }
}
