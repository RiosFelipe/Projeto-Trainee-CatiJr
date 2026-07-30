package br.com.trainee.sistema_de_matriculas.dto;

import br.com.trainee.sistema_de_matriculas.matricula.MatriculaModel;
import java.util.UUID;

public record MatriculaResponseDto(
    UUID id,
    UUID disciplinaId,
    String disciplinaNome,
    String disciplinaCodigo,
    Integer disciplinaCreditos,
    MatriculaModel.StatusMatricula status
) {
    public MatriculaResponseDto(MatriculaModel model) { //construtor auxiliar para converter model em DTO facilmente
        this(
            model.getId(),
            model.getDisciplina().getId(),
            model.getDisciplina().getNome(),
            model.getDisciplina().getCodigo(),
            model.getDisciplina().getCreditos(),
            model.getStatus()
        );
    }
}