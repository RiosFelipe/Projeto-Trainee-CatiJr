package br.com.trainee.sistema_de_matriculas.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, UUID> { //basicamente o que vou usar pra salvar as infos no postgres
    Optional<UserModel> findByEmail(String email);// se eu usar optional, posso usar ispresent() ao inves de fazeer a verificação com !null
}
