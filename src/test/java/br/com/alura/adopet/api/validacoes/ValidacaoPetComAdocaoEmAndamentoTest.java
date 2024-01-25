package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ValidacaoPetComAdocaoEmAndamentoTest {

    @InjectMocks
    private ValidacaoPetComAdocaoEmAndamento validacao;
    @Mock
    private AdocaoRepository adocaoRepository;

    private SolicitacaoAdocaoDto dto;

    @Test
    public void deveEstarComAdocaoEmAndamento() {
        this.dto = new SolicitacaoAdocaoDto(1l,1l, "Motivo qualquer");
        given(adocaoRepository.existsByPetIdAndStatus(dto.idPet(), StatusAdocao.AGUARDANDO_AVALIACAO))
                .willReturn(true);

        Assertions.assertThrows(ValidacaoException.class, ()-> validacao.validar(dto));
    }

    @Test
    public void deveEstarSemAdocaoEmAndamento() {
        this.dto = new SolicitacaoAdocaoDto(1l,1l, "Motivo qualquer");
        given(adocaoRepository.existsByPetIdAndStatus(dto.idPet(), StatusAdocao.AGUARDANDO_AVALIACAO))
                .willReturn(false);

        Assertions.assertDoesNotThrow(()-> validacao.validar(dto));
    }

}