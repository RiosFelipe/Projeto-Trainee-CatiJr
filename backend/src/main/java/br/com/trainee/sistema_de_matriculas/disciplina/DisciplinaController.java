package br.com.trainee.sistema_de_matriculas.disciplina;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/disciplina")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @PostMapping
    public ResponseEntity<DisciplinaModel> create(@RequestBody DisciplinaModel disciplinaModel){
        DisciplinaModel disciplinaCriada = this.disciplinaService.salvar(disciplinaModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaCriada);
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaModel>>listAll(){
        List<DisciplinaModel> disciplinas = this.disciplinaService.listarTodar();
        return ResponseEntity.ok(disciplinas);
    }
    
}
