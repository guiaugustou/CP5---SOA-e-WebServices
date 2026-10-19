package br.com.fiap3esr.autoescola3esr.adapter.in.controller;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.endereco.DadosEndereco;
import br.com.fiap3esr.autoescola3esr.application.service.ConsultaCepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/endereco")
@RequiredArgsConstructor
public class EnderecoController {

    private final ConsultaCepService consultaCepService;

    @GetMapping("/{cep}")
    public ResponseEntity<DadosEndereco> consultarCep(
            @PathVariable String cep) {
        return ResponseEntity.ok(
                consultaCepService.consultar(cep)
        );
    }
}