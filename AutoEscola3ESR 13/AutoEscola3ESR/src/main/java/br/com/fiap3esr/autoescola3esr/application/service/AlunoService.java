
package br.com.fiap3esr.autoescola3esr.application.service;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.mapper.AlunoMapper;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.aluno.DadosAtualicaoAluno;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.aluno.DadosCadastroAluno;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.aluno.DadosDetalhamentoAluno;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.aluno.DadosListagemAluno;
import br.com.fiap3esr.autoescola3esr.application.core.domain.Aluno;
import br.com.fiap3esr.autoescola3esr.application.port.out.AlunoRepository;
import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.EnderecoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final AlunoMapper alunoMapper;
    private final EnderecoMapper enderecoMapper;

    @Transactional
    public DadosDetalhamentoAluno cadastrar(DadosCadastroAluno dados) {
        Aluno aluno = alunoMapper.toDomain(dados);
        aluno = alunoRepository.save(aluno);
        return alunoMapper.toDetails(aluno);
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemAluno> listar(Pageable paginacao) {
        return alunoRepository.findAllByAtivoTrue(paginacao)
                .map(alunoMapper::toList);
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoAluno buscarPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .filter(Aluno::isAtivo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado."
                ));

        return alunoMapper.toDetails(aluno);
    }

    @Transactional
    public DadosDetalhamentoAluno atualizar(DadosAtualicaoAluno dados) {
        Aluno aluno = alunoRepository.findById(dados.id())
                .filter(Aluno::isAtivo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado."
                ));

        aluno.atualizarInformacoes(
                dados.nome(),
                dados.email(),
                dados.telefone(),
                dados.endereco() != null
                        ? enderecoMapper.toVo(dados.endereco())
                        : null
        );

        return alunoMapper.toDetails(aluno);
    }

    @Transactional
    public void desativar(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .filter(Aluno::isAtivo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado."
                ));

        aluno.desativar();
    }
}

