
package br.com.fiap3esr.autoescola3esr.application.service;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.endereco.DadosEndereco;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConsultaCepService {

    private final RestClient restClient = RestClient.create();

    public DadosEndereco consultar(String cep) {
        String cepLimpo = cep.replaceAll("\\D", "");

        if (!cepLimpo.matches("\\d{8}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O CEP deve conter 8 números."
            );
        }

        DadosEndereco endereco = restClient.get()
                .uri("https://viacep.com.br/ws/{cep}/json/", cepLimpo)
                .retrieve()
                .body(DadosEndereco.class);

        if (endereco == null || Boolean.TRUE.equals(endereco.erro())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "CEP não encontrado."
            );
        }

        return endereco;
    }
}
