package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.*;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @InjectMocks
    private AdocaoService service;

    @Mock
    private AdocaoRepository repository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private EmailService emailService;

    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();

    @Mock
    private ValidacaoSolicitacaoAdocao validacao1;

    @Mock
    private ValidacaoSolicitacaoAdocao validacao2;

    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Abrigo abrigo;

    private SolicitacaoAdocaoDto dto;

    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;

    @Mock
    private AprovacaoAdocaoDto aprovacaoAdocaoDto;

    @Mock
    private StatusAdocao statusAdocao;

    @Mock
    private Adocao adocao;

    @Test
    void deveriaSalvarAdocaoAoSolicitar(){
        this.dto = new SolicitacaoAdocaoDto(10l, 20l, "Motivo qualquer");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        service.solicitar(dto);

        then(repository).should().save(adocaoCaptor.capture());
        Adocao adocao = adocaoCaptor.getValue();
        assertEquals(pet, adocao.getPet());
        assertEquals(tutor, adocao.getTutor());
        assertEquals(dto.motivo(), adocao.getMotivo());
    }

    @Test
    void deveriaChamarValidadoresAoSolicitar(){
        this.dto = new SolicitacaoAdocaoDto(10l, 20l, "Motivo qualquer");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);
        validacoes.add(validacao1);
        validacoes.add(validacao2);

        service.solicitar(dto);

        then(validacao1).should().validar(dto);
        then(validacao2).should().validar(dto);
    }

    @Test
    void deveMarcarAprovado(){
        given(repository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(adocao.getTutor()).willReturn(tutor);
        given(adocao.getData()).willReturn( LocalDateTime.now());
        given(adocao.getStatus()).willReturn(statusAdocao.REPROVADO);

        service.aprovar(aprovacaoAdocaoDto);
        then(adocao).should().marcarComoAprovada();

        assertEquals(statusAdocao.APROVADO, adocao.getStatus());
    }
}