package br.com.trainee.sistema_de_matriculas.matricula;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMatriculaRepository extends JpaRepository<MatriculaModel, UUID> {
    List<MatriculaModel> findByAlunoId(UUID alunoId);//busca todas as matriculas de um determinado aluno

    List<MatriculaModel> findByAlunoIdAndStatus(UUID alunoId, MatriculaModel.StatusMatricula status);//busca apenas as matriculas do aluno que estão com um status específico (ex: INSCRITA ou CONCLUIDA)

    Optional<MatriculaModel> findByAlunoIdAndDisciplinaId(UUID alunoId, UUID disciplinaId);//busca se o aluno já tem algum registro nessa disciplina específica
}