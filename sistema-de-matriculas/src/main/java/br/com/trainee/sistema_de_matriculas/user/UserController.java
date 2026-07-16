package br.com.trainee.sistema_de_matriculas.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController //controlador, vai dizer o que cada metodo vai fazer na rota especifica
@RequestMapping("/aluno")
public class UserController {
    
    @Autowired// faz a configuracao automatica do iuserepository
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){ // vai colocar/criar na tabela o usuario (aluno) qunado eu der um post (cadastrar)

        var emailOptional = this.userRepository.findByEmail(userModel.getEmail());

        if (emailOptional.isPresent()){ //se o email ja existe
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email já existe");
        }

        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashed); //seta a senha, mas agora criptografada pra colocar no postgres

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated); //retorna uma response e cada "." é o que ela tem, nesse caso o status e o body em json

    }



}
