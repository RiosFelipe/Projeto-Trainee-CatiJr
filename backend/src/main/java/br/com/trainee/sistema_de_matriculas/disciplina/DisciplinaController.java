package br.com.trainee.sistema_de_matriculas.disciplina;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/disciplina")
public class DisciplinaController {

    @Autowired
    private IDisciplinaRepository disciplinaRepository;

    @PostMapping("/")
    public DisciplinaModel create(@RequestBody DisciplinaModel disciplinaModel){
        var disciplina = this.disciplinaRepository.save(disciplinaModel);
        return disciplina;
    }
    
}
