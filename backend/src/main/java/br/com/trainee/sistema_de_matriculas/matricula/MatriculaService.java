package br.com.trainee.sistema_de_matriculas.matricula;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.trainee.sistema_de_matriculas.disciplina.DisciplinaModel;
import br.com.trainee.sistema_de_matriculas.disciplina.HorarioAula;
import br.com.trainee.sistema_de_matriculas.disciplina.IDisciplinaRepository;
import br.com.trainee.sistema_de_matriculas.dto.MatriculaRequestDto;
import br.com.trainee.sistema_de_matriculas.dto.MatriculaResponseDto;
import br.com.trainee.sistema_de_matriculas.user.IUserRepository;
import br.com.trainee.sistema_de_matriculas.user.UserModel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final IMatriculaRepository matriculaRepository;
    private final IDisciplinaRepository disciplinaRepository;
    private final IUserRepository userRepository;

    @Transactional
    public MatriculaResponseDto realizarMatricula(UUID alunoId, MatriculaRequestDto dto) {

        UserModel aluno = userRepository.findById(alunoId)//verifica se o aluno existe
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

       
        DisciplinaModel disciplina = disciplinaRepository.findById(dto.disciplinaId()) //verifica se a disciplina existe
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada."));

        Optional<MatriculaModel> matriculaExistente = matriculaRepository.findByAlunoIdAndDisciplinaId(alunoId, dto.disciplinaId());

        if (matriculaExistente.isPresent()) { //se a matricula ja existe, seja por conclusao ou inscricao previa
            MatriculaModel.StatusMatricula statusAtual = matriculaExistente.get().getStatus();
            if (statusAtual == MatriculaModel.StatusMatricula.INSCRITA || statusAtual == MatriculaModel.StatusMatricula.CONCLUIDA) {
                throw new RuntimeException("Você já está inscrito ou concluiu esta disciplina.");
            }
        }

        if (disciplina.getVagas() <= 0) {//verifica se tem vagas sobrando pra fazer a matricula
            throw new RuntimeException("Não há vagas disponíveis para esta disciplina.");
        }

        //salva as disciplinas concluidas
        List<MatriculaModel> disciplinasConcluidas = matriculaRepository.findByAlunoIdAndStatus(alunoId, MatriculaModel.StatusMatricula.CONCLUIDA);

        List<UUID> idsConcluidos = disciplinasConcluidas.stream()
                .map(m -> m.getDisciplina().getId())
                .toList();

        
        for (DisciplinaModel preRequisito : disciplina.getPreRequisitos()) { //verifica os pre requisitos
            if (!idsConcluidos.contains(preRequisito.getId())) {
                throw new RuntimeException("Pré-requisito não cumprido: " + preRequisito.getNome());
            }
        }

        // salva as matriculas ativas do semestre atual, as inscritas no caso
        List<MatriculaModel> matriculasAtivas = matriculaRepository.findByAlunoIdAndStatus(alunoId, MatriculaModel.StatusMatricula.INSCRITA);

        int creditosAtuais = matriculasAtivas.stream()//soma os creditos das matriculas ativas
                .mapToInt(m -> m.getDisciplina().getCreditos())
                .sum();

        //se a disciplina que eu for puxar estourar meu limite de 24 creditos
        if (creditosAtuais + disciplina.getCreditos() > 24) {
            throw new RuntimeException("Limite de créditos excedido (Máximo: 24 créditos). Créditos atuais: " + creditosAtuais);
        }

        //loop que vai verificar se vai haver conflito de horarios com materia que vou me inscrever
        for (MatriculaModel mat : matriculasAtivas) {
            for (HorarioAula hExistente : mat.getDisciplina().getHorarios()) {
                for (HorarioAula hNovo : disciplina.getHorarios()) {
                    if (hExistente.getDiaDaSemana().equals(hNovo.getDiaDaSemana()) &&
                        hExistente.getHorarioInicio().equals(hNovo.getHorarioInicio())) {
                        throw new RuntimeException("Conflito de horário com a disciplina: " + mat.getDisciplina().getNome());
                    }
                }
            }
        }

        // cria a matricula/atualiza
        disciplina.setVagas(disciplina.getVagas() - 1); //atualiza as vagas apos inscricao
        disciplinaRepository.save(disciplina);

        MatriculaModel novaMatricula = matriculaExistente.orElse(new MatriculaModel());
        novaMatricula.setAluno(aluno);
        novaMatricula.setDisciplina(disciplina);
        novaMatricula.setStatus(MatriculaModel.StatusMatricula.INSCRITA);

        MatriculaModel matriculaSalva = matriculaRepository.save(novaMatricula);

        // retorna mapeado para o response dto
        return new MatriculaResponseDto(matriculaSalva);
    }

    // vai listar "minhas disciplinas" convertidas em dtos
    @Transactional(readOnly = true)
    public List<MatriculaResponseDto> listarMatriculasDoAluno(UUID alunoId) {
        List<MatriculaModel> matriculas = matriculaRepository.findByAlunoId(alunoId);

        return matriculas.stream()
                .map(MatriculaResponseDto::new)
                .toList();
    }

    // funcao que vai cancelar a mattricula
    @Transactional
    public void cancelarMatricula(UUID alunoId, UUID matriculaId) {
        MatriculaModel matricula = matriculaRepository.findById(matriculaId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada."));

        if (!matricula.getAluno().getId().equals(alunoId)) {
            throw new RuntimeException("Você não tem permissão para cancelar esta disciplina.");
        }

        if (matricula.getStatus() != MatriculaModel.StatusMatricula.INSCRITA) {
            throw new RuntimeException("Apenas matrículas com status 'INSCRITA' podem ser canceladas.");
        }

        DisciplinaModel disciplina = matricula.getDisciplina();
        disciplina.setVagas(disciplina.getVagas() + 1); //se cancelar a inscricao, devolve uma vaga
        disciplinaRepository.save(disciplina);

        matriculaRepository.delete(matricula);
    }
}