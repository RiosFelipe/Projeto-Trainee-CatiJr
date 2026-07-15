package br.com.trainee.sistema_de_matriculas.disciplina;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    private String horario;
    private String descricao;
    private String nomeProfessor;

    @CreationTimestamp
    private LocalDateTime createdAt;
    //


}
