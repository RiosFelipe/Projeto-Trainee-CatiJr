package br.com.trainee.sistema_de_matriculas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDto(
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "insira um e-mail válido.")
    String email,

    @NotBlank(message = "o código de verificação é obrigatório.")
    String code,

    @NotBlank(message = "A nova senha é obrigatória.")
    @Size(min =6, message = "A nova senha deve ter no mínimo 6 caracteres")
    String newPassword
){}