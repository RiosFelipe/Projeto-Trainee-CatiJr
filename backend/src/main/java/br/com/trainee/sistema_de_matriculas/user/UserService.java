package br.com.trainee.sistema_de_matriculas.user;

import org.springframework.stereotype.Service;
import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.trainee.sistema_de_matriculas.dto.LoginDto;
import br.com.trainee.sistema_de_matriculas.dto.ResetPasswordDto;
import br.com.trainee.sistema_de_matriculas.dto.SignUpDto;
import br.com.trainee.sistema_de_matriculas.security.JwtService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor //substitui o @Autowired
public class UserService {

    private final IUserRepository userRepository;
    private final JwtService jwtService;

    public UserModel signup(SignUpDto dto) {
        var emailOptional = this.userRepository.findByEmail(dto.email());//se o email ja existe
        if (emailOptional.isPresent()) {
            throw new IllegalArgumentException("email já existe");
        }

        String passwordHashed = BCrypt.withDefaults().hashToString(12, dto.password().toCharArray());//guarda a senha criptografada

        UserModel user = new UserModel(); 
        user.setNome(dto.nome());
        user.setEmail(dto.email());
        user.setPassword(passwordHashed);

        return this.userRepository.save(user); //salva o cadastro no postgres
    }

    public String login(LoginDto loginDto) {
        UserModel user = this.userRepository.findByEmail(loginDto.email())//busco o usuário pelo email do dto
                .orElseThrow(() -> new IllegalArgumentException("email ou senha incorretos."));

        var validPassword = BCrypt.verifyer().verify(loginDto.password().toCharArray(), user.getPassword());//vai verificar se as senhas sao iguais
        if (!validPassword.verified) {
            throw new IllegalArgumentException("email ou senha incorretos.");
        }
        return this.jwtService.generateToken(user.getId().toString());//gera o token jwt logo apos o login
    }

    public void esqueciSenha(ResetPasswordDto dto) {
        if (!"6716".equals(dto.code())) { //faz uma validacao com um codigo fixo por mim, no caso vai ser "6716"
            throw new IllegalArgumentException("Código de verificação inválido!");
        }

        UserModel user = this.userRepository.findByEmail(dto.email())// busco o usuario pelo email do dto
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        String newPasswordHashed = BCrypt.withDefaults().hashToString(12, dto.newPassword().toCharArray());//criptpografa a senha
        user.setPassword(newPasswordHashed);

        this.userRepository.save(user); //salva no postgres
    }
}