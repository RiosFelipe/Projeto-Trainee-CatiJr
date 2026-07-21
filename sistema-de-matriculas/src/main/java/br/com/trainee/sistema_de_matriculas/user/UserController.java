package br.com.trainee.sistema_de_matriculas.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.trainee.sistema_de_matriculas.security.JwtService;
import br.com.trainee.sistema_de_matriculas.user.dto.LoginDto;
import br.com.trainee.sistema_de_matriculas.user.dto.ResetPasswordDto;

@RestController //controlador, vai dizer o que cada metodo vai fazer na rota especifica
@RequestMapping("/aluno")
public class UserController {
    
    @Autowired// faz a configuracao automatica do iuserepository
    private IUserRepository userRepository;

    @Autowired
    private JwtService jwtService; 

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){ // vai colocar/criar na tabela o usuario (aluno) qunado eu der um post (cadastrar)
   
        //verificacao dos campos pra ver se nenhum deles chega vazio
        if (userModel.getNome() == null || userModel.getNome().trim().isEmpty() ||
            userModel.getEmail() == null || userModel.getEmail().trim().isEmpty() ||
            userModel.getPassword() == null || userModel.getPassword().trim().isEmpty()) {
            
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Preencha todos os campos.");
        }

        //verificacao do formato de email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (!userModel.getEmail().matches(emailRegex)) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Insira um endereço de e-mail válido.");
    }
        //trava no tamanho da senha
        if (userModel.getPassword().length() < 6) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("A senha deve ter no mínimo 6 caracteres.");
        }
        var emailOptional = this.userRepository.findByEmail(userModel.getEmail());

        if (emailOptional.isPresent()){ //se o email ja existe
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email já existe");
        }

        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashed); //seta a senha, mas agora criptografada pra colocar no postgres

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated); //retorna uma response e cada "." é o que ela tem, nesse caso o status e o body em json

    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity esqueciSenha(@RequestBody ResetPasswordDto dto) {

        //faz uma validacao com um codigo fixo por mim, no caso vai ser "6716"
        if (!"6716".equals(dto.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código de verificação inválido!");
        }

        //valida nova senha
        if (dto.getNewPassword() == null || dto.getNewPassword().length() < 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A nova senha deve ter no mínimo 6 caracteres.");
        }

        //busca o usuário no banco pelo email
        var userOptional = this.userRepository.findByEmail(dto.getEmail());

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        //pega o usuário, criptografa a nova senha e salva no postgres
        var user = userOptional.get();
        var newPasswordHashed = BCrypt.withDefaults().hashToString(12, dto.getNewPassword().toCharArray());
        
        user.setPassword(newPasswordHashed);
        this.userRepository.save(user);

        return ResponseEntity.ok().body("Senha alterada com sucesso! Você já pode fazer login.");
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto){

        var userOptional = this.userRepository.findByEmail(loginDto.getEmail()); // busco o usuario pelo email do dto

        if (!userOptional.isPresent()){ // se nao encontrou o email no banco de dados
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("email ou senha incorretos.");
        }

        var user = userOptional.get(); //pega o usuario dentro do optional

        var validPassword = BCrypt.verifyer().verify(loginDto.getPassword().toCharArray(), user.getPassword()); //vai verificar se as senhas sao iguais

        if (!validPassword.verified){ // se nao forem iguais
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("email ou senha incorretos.");
        }

        String token = this.jwtService.generateToken(user.getId().toString()); // gera o token jwt logo apos o login

        return ResponseEntity.ok().body(token);// retorna a response com o token

    }

}
