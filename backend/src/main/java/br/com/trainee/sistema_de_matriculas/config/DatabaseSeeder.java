package br.com.trainee.sistema_de_matriculas.config;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.trainee.sistema_de_matriculas.disciplina.DisciplinaModel;
import br.com.trainee.sistema_de_matriculas.disciplina.HorarioAula;
import br.com.trainee.sistema_de_matriculas.disciplina.IDisciplinaRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    
    @Autowired
    private IDisciplinaRepository disciplinaRepository;

    @Override
    public void run (String... args) throws Exception{ //vai povoar so uma vez o postgres
        if (this.disciplinaRepository.count()>0){
            return;
        }
    
    //Calculo Diferencial e Integral
    DisciplinaModel calculo1 = new DisciplinaModel();

    HorarioAula h1= new HorarioAula("SEGUNDA",LocalTime.of(8,0),LocalTime.of(10,0));
    HorarioAula h2= new HorarioAula("QUARTA",LocalTime.of(10,0),LocalTime.of(12,0));
    HorarioAula h3= new HorarioAula("SEXTA",LocalTime.of(8,0),LocalTime.of(10,0));

    calculo1.setCodigo("CDI");
    calculo1.setNome("Cálculo Diferencial e Integral");
    calculo1.setCreditos(6);
    calculo1.setVagas(60);
    calculo1.setHorarios(List.of(h1,h2,h3)); //cria a lista de horarios
    calculo1.setDescricao("Introdução ao estudo de limites, derivadas e integrais.");
    calculo1.setNomeProfessor("Fabio Gomes Figueira");
    calculo1.setStatus(DisciplinaModel.StatusDisciplina.DISPONIVEL);
    calculo1.setPreRequisitos(List.of()); //sem pre requisitos
    calculo1 = this.disciplinaRepository.save(calculo1);

    //Construcao de Algoritmos e Programacao

    DisciplinaModel cap = new DisciplinaModel();

    HorarioAula h4= new HorarioAula("SEGUNDA",LocalTime.of(14,0),LocalTime.of(18,0));
    HorarioAula h5= new HorarioAula("QUARTA",LocalTime.of(14,0),LocalTime.of(18,0));

    cap.setCodigo("CAP");
    cap.setNome("Construção de Algoritmos e Programação");
    cap.setCreditos(8);
    cap.setVagas(60);
    cap.setHorarios(List.of(h4,h5)); 
    cap.setDescricao("Introdução à lógica de programação e desenvolvimento de algoritmos.");
    cap.setNomeProfessor("Joice Lee Otsuka");
    cap.setStatus(DisciplinaModel.StatusDisciplina.DISPONIVEL);
    cap.setPreRequisitos(List.of()); 
    cap = this.disciplinaRepository.save(cap);
        
    //Introducao ao Pensamento Algoritmico

    DisciplinaModel ipa = new DisciplinaModel();

    HorarioAula h6= new HorarioAula("SEXTA",LocalTime.of(10,0),LocalTime.of(12,0));

    ipa.setCodigo("IPA");
    ipa.setNome("Introdução ao Pensamento Algoritmico");
    ipa.setCreditos(2);
    ipa.setVagas(60);
    ipa.setHorarios(List.of(h6)); 
    ipa.setDescricao("Introdução ao raciocínio computacional e à lógica.");
    ipa.setNomeProfessor("Marcela Xavier Ribeiro");
    ipa.setStatus(DisciplinaModel.StatusDisciplina.DISPONIVEL);
    ipa.setPreRequisitos(List.of()); 
    ipa = this.disciplinaRepository.save(ipa);

    //Logica Digital

    DisciplinaModel ld = new DisciplinaModel();

    HorarioAula h7= new HorarioAula("TERCA",LocalTime.of(14,0),LocalTime.of(16,0));
    HorarioAula h8= new HorarioAula("QUINTA",LocalTime.of(10,0),LocalTime.of(12,0));
    HorarioAula h9= new HorarioAula("QUINTA",LocalTime.of(16,0),LocalTime.of(18,0));

    ld.setCodigo("LD");
    ld.setNome("Lógica Digital");
    ld.setCreditos(6);
    ld.setVagas(60);
    ld.setHorarios(List.of(h7,h8,h9)); 
    ld.setDescricao("Estudo de portas lógicas e circuitos digitais.");
    ld.setNomeProfessor("Ricardo Menotti");
    ld.setStatus(DisciplinaModel.StatusDisciplina.DISPONIVEL);
    ld.setPreRequisitos(List.of());
    ld = this.disciplinaRepository.save(ld);

    //Algoritmos e Estrutura de Dados 1

    DisciplinaModel aed1 = new DisciplinaModel();

    HorarioAula h10= new HorarioAula("TERCA",LocalTime.of(8,0),LocalTime.of(12,0));

    aed1.setCodigo("AED1");
    aed1.setNome("Algoritmos e Estrutura de Dados 1");
    aed1.setCreditos(4);
    aed1.setVagas(60);
    aed1.setHorarios(List.of(h10)); 
    aed1.setDescricao("Estudo de estruturas de dados e algoritmos eficientes.");
    aed1.setNomeProfessor("Roberto Ferrari Junior");
    aed1.setStatus(DisciplinaModel.StatusDisciplina.DISPONIVEL);
    aed1.setPreRequisitos(List.of(cap));// precisa ter feito cap pra fazer aed1
    aed1 = this.disciplinaRepository.save(aed1);
    

    //Metodologia Cientifica

    DisciplinaModel mc = new DisciplinaModel();

    HorarioAula h11= new HorarioAula("SEXTA",LocalTime.of(10,0),LocalTime.of(12,0));

    mc.setCodigo("MC");
    mc.setNome("Metodologia Científica");
    mc.setCreditos(2);
    mc.setVagas(60);
    mc.setHorarios(List.of(h11)); 
    mc.setDescricao("Fundamentos da pesquisa e do método científico.");
    mc.setNomeProfessor("Vânia Paula de Almeida Neris");
    mc.setStatus(DisciplinaModel.StatusDisciplina.DISPONIVEL);
    mc.setPreRequisitos(List.of());
    mc = this.disciplinaRepository.save(mc);
}

}
