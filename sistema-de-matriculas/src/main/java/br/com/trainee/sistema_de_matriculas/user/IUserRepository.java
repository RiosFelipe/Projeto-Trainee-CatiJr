package br.com.trainee.sistema_de_matriculas.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, UUID> { //basicamente o que vou usar pra salvar as infos no postgres
    UserModel findByEmail(String email);
}
