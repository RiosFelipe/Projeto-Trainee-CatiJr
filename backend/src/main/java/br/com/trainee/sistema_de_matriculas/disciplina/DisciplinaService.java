package br.com.trainee.sistema_de_matriculas.disciplina;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DisciplinaService {

    private final IDisciplinaRepository disciplinaRepository;

    public DisciplinaModel salvar(DisciplinaModel disciplinaModel){ //cadastra as disciplinas
        return this.disciplinaRepository.save(disciplinaModel);
    }

    public List<DisciplinaModel> listarTodar(){ //lista todas as disciplinas
        return this.disciplinaRepository.findAll();
    }
}
