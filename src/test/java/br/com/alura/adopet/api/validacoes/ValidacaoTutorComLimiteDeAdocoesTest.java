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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComLimiteDeAdocoesTest {

    @InjectMocks
    private ValidacaoTutorComLimiteDeAdocoes validacao;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Spy
    private List<Adocao> adocoes = new ArrayList<>();

    @Mock
    private Adocao adocao;

    @Mock
    private Tutor tutor;

    private SolicitacaoAdocaoDto dto;

    @Test
    void deveChegarAoLimiteDeAdocoes(){
        int numeroDeAdocoes = 0;
        this.dto = new SolicitacaoAdocaoDto(1l,1l, "Motivo qualquer");
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(adocaoRepository.findAll()).willReturn(adocoes);

        while(numeroDeAdocoes < 5){
            given(adocao.getTutor()).willReturn(tutor);
            given(adocao.getStatus()).willReturn(StatusAdocao.APROVADO);
            adocoes.add(adocao);
            numeroDeAdocoes+= 1;
        }

        assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void deveTerAdocoesDisponivel(){
        int numeroDeAdocoes = 0;
        this.dto = new SolicitacaoAdocaoDto(1l,1l, "Motivo qualquer");
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(adocaoRepository.findAll()).willReturn(adocoes);

        given(adocao.getTutor()).willReturn(tutor);
        given(adocao.getStatus()).willReturn(StatusAdocao.REPROVADO);
        adocoes.add(adocao);

        given(adocao.getTutor()).willReturn(tutor);
        given(adocao.getStatus()).willReturn(StatusAdocao.AGUARDANDO_AVALIACAO);
        adocoes.add(adocao);

        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

}