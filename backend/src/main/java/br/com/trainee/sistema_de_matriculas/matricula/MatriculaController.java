package br.com.trainee.sistema_de_matriculas.matricula;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trainee.sistema_de_matriculas.dto.MatriculaRequestDto;
import br.com.trainee.sistema_de_matriculas.dto.MatriculaResponseDto;
import br.com.trainee.sistema_de_matriculas.user.UserModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/matriculas")
@RequiredArgsConstructor
public class MatriculaController {
    
    private final MatriculaService matriculaService;

    @PostMapping // vai realizar uma nova inscricao em uma disciplina
    public ResponseEntity<MatriculaResponseDto> matricular(@RequestBody @Valid MatriculaRequestDto dto, Authentication authentication){
        UserModel alunoLogado = (UserModel) authentication.getPrincipal();
        MatriculaResponseDto response = matriculaService.realizarMatricula(alunoLogado.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/minhas-disciplinas") //lista as matriculas das disciplinas inscritas do aluno logado
    public ResponseEntity <List<MatriculaResponseDto>> listarMinhasMatriculas(Authentication authentication){
        UserModel alunoLogado = (UserModel) authentication.getPrincipal();
        List<MatriculaResponseDto> matriculas = matriculaService.listarMatriculasDoAluno(alunoLogado.getId());
        return ResponseEntity.ok(matriculas);
    }
    
    @DeleteMapping("/{id}")// cancela a matricula pelo id
    public ResponseEntity<Void> cancelarMatricula(@PathVariable UUID id, Authentication authentication){
        UserModel alunoLogado = (UserModel) authentication.getPrincipal();
        matriculaService.cancelarMatricula(alunoLogado.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
