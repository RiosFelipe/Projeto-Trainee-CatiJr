package br.com.trainee.sistema_de_matriculas.matricula;

import java.util.UUID;

import br.com.trainee.sistema_de_matriculas.disciplina.DisciplinaModel;
import br.com.trainee.sistema_de_matriculas.user.UserModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_matricula")
public class MatriculaModel {

    @Id
    @GeneratedValue(generator = "UUID") //cada matricula tem id unico
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) //nao pode estar vazio, alem disso um aluno pode ter varias matriculas em disciplinas distintas
    private UserModel aluno;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)//mesma coisa, mas uma disciplina pode ter varias matriculas de alunos distintos
    private DisciplinaModel disciplina;

    public enum StatusMatricula{ //os status da matricula
        DISPONIVEL,
        INDISPONIVEL,
        INSCRITA,
        CONCLUIDA,
        REPROVADA
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMatricula status;
    
}
