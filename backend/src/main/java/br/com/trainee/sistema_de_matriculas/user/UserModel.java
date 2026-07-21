package br.com.trainee.sistema_de_matriculas.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Data 
@Entity(name = "tb_users") //tabela dos users, que vai ser dos alunos
public class UserModel {

    @Id
    @GeneratedValue(generator = "UUID") //gerador de id automatico
    private UUID id;

    @Column(unique = true)// cada aluno tem que ter um email unico, o resto pode ser igual
    private String email;
    private String nome;
    private String password;
    
    @CreationTimestamp //vai marcar o tempo quando um usuario foi criado
    private LocalDateTime createdAt;
}
