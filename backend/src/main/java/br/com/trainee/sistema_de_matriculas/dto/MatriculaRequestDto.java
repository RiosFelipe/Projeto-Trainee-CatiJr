package br.com.trainee.sistema_de_matriculas.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record MatriculaRequestDto(
    @NotNull(message = "O id da disciplina é obrigatório")
    UUID disciplinaId
) {}
