package br.com.trainee.sistema_de_matriculas.disciplina;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity(name = "tb_disciplina")
public class DisciplinaModel {
    
    @Id
    @GeneratedValue(generator = "UUID" ) //gera id automatico da disciplina
    private UUID id;

    @Column(unique = true)// cada disciplina tem um codigo unico
    private String codigo;
    private String nome;
    private int creditos;
    private int vagas;
    private String descricao;
    private String nomeProfessor;

    @ElementCollection 
    @CollectionTable(
        name = "tb_disciplina_horarios",
        joinColumns = @JoinColumn(name = "disciplina_id")
    )
    private List<HorarioAula> horarios; //lista com dia e horarios das aulas

    public enum StatusDisciplina{// cria uma lista fixa de opcoes pra disciplina
        DISPONIVEL,
        INDISPONIVEL
    }

    @Enumerated(EnumType.STRING) //salva o status como string
    private StatusDisciplina status;

    @ManyToMany //relacao entre registros da mesma tabela, nesse caso da disciplina e da disciplina pre requisito 
    @JoinTable(
        name = "tb_disciplina_pre_requisitos", //tabela de pre requisitos
        joinColumns = @JoinColumn(name = "disciplina_id"), //coluna que se refere a disciplina principal
        inverseJoinColumns = @JoinColumn(name = "pre_requisito_id")//coluna que se refere a disciplina de pre requisito
    )

    private List<DisciplinaModel> preRequisitos; //lista de pre requisitos de cada disciplina

    @CreationTimestamp
    private LocalDateTime createdAt;

}
