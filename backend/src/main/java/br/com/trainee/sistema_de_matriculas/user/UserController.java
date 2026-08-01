package br.com.trainee.sistema_de_matriculas.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import br.com.trainee.sistema_de_matriculas.dto.LoginDto;
import br.com.trainee.sistema_de_matriculas.dto.ResetPasswordDto;
import br.com.trainee.sistema_de_matriculas.dto.SignUpDto;
import br.com.trainee.sistema_de_matriculas.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/aluno")
@RequiredArgsConstructor //nao precisa usar autowired 
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody SignUpDto dto) {
        UserModel userCriado = this.userService.signup(dto); //o service vai fazer toda a parte de verificacao
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDto.from(userCriado));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) {
        String token = this.userService.login(loginDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> esqueciSenha(@Valid @RequestBody ResetPasswordDto dto) {
        this.userService.esqueciSenha(dto);
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> perfil(Authentication authentication) {
        UserModel alunoLogado = (UserModel) authentication.getPrincipal();
        UserModel user = this.userService.buscarPorId(alunoLogado.getId());
        return ResponseEntity.ok(UserResponseDto.from(user));
    }
}