package br.com.trainee.sistema_de_matriculas.user.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
    private String email;
    private String code;
    private String newPassword;
}