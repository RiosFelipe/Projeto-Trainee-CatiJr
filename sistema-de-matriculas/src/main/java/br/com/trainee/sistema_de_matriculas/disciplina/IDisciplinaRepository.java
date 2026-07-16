package br.com.trainee.sistema_de_matriculas.disciplina;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IDisciplinaRepository extends JpaRepository<DisciplinaModel,UUID> {
    
}
