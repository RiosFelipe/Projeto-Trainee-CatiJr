package br.com.trainee.sistema_de_matriculas.dto;

import java.util.UUID;
import br.com.trainee.sistema_de_matriculas.user.UserModel;

public record UserResponseDto(UUID id, String nome, String email) {
    
    public static UserResponseDto from(UserModel user) {
        return new UserResponseDto(
            user.getId(),
            user.getNome(),
            user.getEmail()
        );
    }
}