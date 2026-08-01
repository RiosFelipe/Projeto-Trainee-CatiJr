export type Page = 'login' | 'signup' | 'dashboard' | 'forgot-password'

export interface User {
  id: string
  nome: string
  email: string
}

export interface Horario {
  diaDaSemana: string
  horarioInicio: string
  horarioFim: string
}

export interface PreRequisito {
  id: string
  codigo: string
  nome: string
}

export interface Disciplina {
  id: string
  codigo: string
  nome: string
  creditos: number
  vagas: number
  descricao: string
  nomeProfessor: string
  status: 'DISPONIVEL' | 'INDISPONIVEL'
  horarios: Horario[]
  preRequisitos: PreRequisito[]
}

export interface Matricula {
  id: string
  disciplinaId: string
  disciplinaNome: string
  disciplinaCodigo: string
  disciplinaCreditos: number
  status: StatusMatricula
}

export type StatusMatricula = 'INSCRITA' | 'CONCLUIDA' | 'REPROVADA'

export type ValidationError = 'PREREQUISITO' | 'CREDITOS' | 'HORARIO'

export interface CatalogDisciplina extends Disciplina {
  matriculaStatus: StatusMatricula | null
  matriculaId: string | null
  validationError: ValidationError | null
}
