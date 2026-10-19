
package br.com.fiap3esr.autoescola3esr.adapter.in.controller;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.aluno.DadosAtualicaoAluno;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.aluno.DadosCadastroAluno;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.aluno.DadosDetalhamentoAluno;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.aluno.DadosListagemAluno;
import br.com.fiap3esr.autoescola3esr.application.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoAluno> cadastrar(
            @RequestBody @Valid DadosCadastroAluno dados,
            UriComponentsBuilder uriBuilder) {

        DadosDetalhamentoAluno aluno = alunoService.cadastrar(dados);

        var uri = uriBuilder
                .path("/alunos/{id}")
                .buildAndExpand(aluno.id())
                .toUri();

        return ResponseEntity.created(uri).body(aluno);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemAluno>> listar(
            @PageableDefault(size = 10, sort = "nome") Pageable paginacao) {

        return ResponseEntity.ok(alunoService.listar(paginacao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoAluno> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(alunoService.buscarPorId(id));
    }

    @PutMapping
    public ResponseEntity<DadosDetalhamentoAluno> atualizar(
            @RequestBody @Valid DadosAtualicaoAluno dados) {

        return ResponseEntity.ok(alunoService.atualizar(dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        alunoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
