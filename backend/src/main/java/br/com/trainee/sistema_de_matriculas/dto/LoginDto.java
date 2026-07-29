package br.com.trainee.sistema_de_matriculas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDto(
    @NotBlank(message = "O e-mail é obrigatório.") //a anotation ja faz o serviço de verificar se estar vazio
    @Email(message = "insira um e-mail válido.") //faz todas a verificacao que eu fiz com o regex
    String email,

    @NotBlank(message = "A senha é obrigatória")
    String password

){}
