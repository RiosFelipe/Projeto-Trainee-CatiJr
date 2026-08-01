package br.com.trainee.sistema_de_matriculas.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import br.com.trainee.sistema_de_matriculas.user.UserModel;

public record UserResponseDto(UUID id, String nome, String email, LocalDateTime createdAt) {
    
    public static UserResponseDto from(UserModel user) {
        return new UserResponseDto(
            user.getId(),
            user.getNome(),
            user.getEmail(),
            user.getCreatedAt()
        );
    }
}