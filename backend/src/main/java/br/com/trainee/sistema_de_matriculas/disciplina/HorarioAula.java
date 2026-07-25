package br.com.trainee.sistema_de_matriculas.disciplina;

import java.time.LocalTime;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor//construtor vazio
@AllArgsConstructor//construtor que vou usar pra criar os horarios
@Embeddable //indica que essa classe vai ser embutida em outra tabela
public class HorarioAula {
    private String diaDaSemana; //dia da semana que ocorre a disciplina
    private LocalTime horarioInicio; //horario que comeca a aula
    private LocalTime horarioFim; //horario que termina a aula
}
