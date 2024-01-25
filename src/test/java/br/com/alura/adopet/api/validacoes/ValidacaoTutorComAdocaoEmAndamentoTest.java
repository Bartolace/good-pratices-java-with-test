package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComAdocaoEmAndamentoTest {

    @InjectMocks
    private ValidacaoTutorComAdocaoEmAndamento validacao;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private Adocao adocao;

    @Spy
    private List<Adocao> adocoes = new ArrayList<>();

    @Mock
    private Tutor tutor;

    private SolicitacaoAdocaoDto dto;


    @Test
    void deveEstarAguardandoAvaliacao(){
        this.dto = new SolicitacaoAdocaoDto(1l,1l,"Motivo qualquer");
        given(adocao.getTutor()).willReturn(tutor);
        given(adocao.getStatus()).willReturn(StatusAdocao.AGUARDANDO_AVALIACAO);
        adocoes.add(adocao);
        given(adocaoRepository.findAll()).willReturn(adocoes);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);

        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void deveEstarAvaliacaoDispovivel(){
        this.dto = new SolicitacaoAdocaoDto(1l,1l,"Motivo qualquer");
        given(adocao.getTutor()).willReturn(tutor);
        given(adocao.getStatus()).willReturn(StatusAdocao.APROVADO);
        adocoes.add(adocao);
        given(adocao.getTutor()).willReturn(tutor);
        given(adocao.getStatus()).willReturn(StatusAdocao.REPROVADO);
        adocoes.add(adocao);
        given(adocaoRepository.findAll()).willReturn(adocoes);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);

        Assertions.assertDoesNotThrow( () -> validacao.validar(dto));
    }

}