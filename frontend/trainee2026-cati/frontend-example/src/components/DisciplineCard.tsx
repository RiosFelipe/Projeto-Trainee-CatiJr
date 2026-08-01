import { useState } from 'react'
import { GraduationCapIcon } from '../assets/icons'
import type { CatalogDisciplina, StatusMatricula } from '../types'

interface DisciplineCardProps {
  disciplina: CatalogDisciplina
  onEnroll: (id: string) => void
  loading: boolean
}

const statusLabels: Record<StatusMatricula, string> = {
  INSCRITA: 'Inscrito',
  CONCLUIDA: 'Concluída',
  REPROVADA: 'Reprovada',
}

const validationLabels: Record<string, { text: string; color: string }> = {
  PREREQUISITO: { text: 'Pré-requisito não cumprido', color: 'bg-red-100 text-red-700 border-red-200' },
  CREDITOS: { text: 'Limite de créditos excedido (máx. 24)', color: 'bg-orange-100 text-orange-700 border-orange-200' },
  HORARIO: { text: 'Conflito de horário', color: 'bg-red-100 text-red-700 border-red-200' },
}

function formatHorario(dia: string, inicio: string, fim: string) {
  const dias: Record<string, string> = {
    SEGUNDA: 'Seg',
    TERCA: 'Ter',
    QUARTA: 'Qua',
    QUINTA: 'Qui',
    SEXTA: 'Sex',
    SABADO: 'Sáb',
  }
  return `${dias[dia] || dia} ${inicio.substring(0, 5)}-${fim.substring(0, 5)}`
}

export default function DisciplineCard({ disciplina, onEnroll, loading }: DisciplineCardProps) {
  const [showModal, setShowModal] = useState(false)
  const isEnrolled = disciplina.matriculaStatus !== null
  const isReprovada = disciplina.matriculaStatus === 'REPROVADA'
  const isActuallyEnrolled = isEnrolled && !isReprovada
  const hasError = disciplina.validationError !== null
  const isIndisponivel = disciplina.status === 'INDISPONIVEL'

  return (
    <div className="bg-white border border-ui-border rounded-xl drop-shadow-[0px_1px_1px_rgba(0,0,0,0.05)] flex flex-col p-5 gap-4 transition-shadow hover:shadow-md h-full">
      <div className="flex items-start justify-between gap-3">
        <div className="flex items-center gap-3 min-w-0">
          <div className="bg-brand-light flex items-center justify-center w-10 h-10 rounded-lg shrink-0">
            <GraduationCapIcon width={18} height={14} color="#3525cd" />
          </div>
          <div className="min-w-0">
            <h3 className="font-semibold text-base text-ui-dark leading-tight truncate">
              {disciplina.nome}
            </h3>
            <p className="text-sm text-ui-muted">{disciplina.codigo}</p>
          </div>
        </div>

        <div className="flex items-center gap-1.5 shrink-0">
          <span className="text-xs font-semibold text-brand-primary bg-brand-light px-2 py-0.5 rounded">
            {disciplina.creditos} cr
          </span>
          {disciplina.status === 'INDISPONIVEL' && (
            <span className="text-xs font-medium text-ui-muted bg-gray-100 px-2 py-0.5 rounded">
              Indisponível
            </span>
          )}
        </div>
      </div>

      <div className="flex flex-wrap gap-4 text-sm">
        <div className="flex items-center gap-1.5">
          <span className="text-ui-muted">Vagas:</span>
          <span className="font-medium text-ui-dark">{disciplina.vagas}</span>
        </div>
        <div className="flex items-center gap-1.5">
          <span className="text-ui-muted">Prof:</span>
          <span className="font-medium text-ui-dark truncate max-w-[160px]">{disciplina.nomeProfessor}</span>
        </div>
      </div>

      {disciplina.horarios.length > 0 && (
        <div className="flex flex-wrap gap-1.5">
          {disciplina.horarios.map((h, i) => (
            <span
              key={i}
              className="text-xs text-ui-dark bg-ui-bg border border-ui-border px-2.5 py-1 rounded-md font-medium"
            >
              {formatHorario(h.diaDaSemana, h.horarioInicio, h.horarioFim)}
            </span>
          ))}
        </div>
      )}

      {disciplina.preRequisitos.length > 0 && (
        <div className="flex items-center gap-1.5 text-xs">
          <span className="text-ui-muted">Pré-requisitos:</span>
          {disciplina.preRequisitos.map((pre) => (
            <span key={pre.id} className="font-medium text-ui-dark bg-ui-bg px-2 py-0.5 rounded">
              {pre.codigo}
            </span>
          ))}
        </div>
      )}

      <button
        onClick={() => setShowModal(true)}
        className="text-xs font-medium text-brand-primary hover:text-indigo-700 transition-colors self-start"
      >
        Ver detalhes
      </button>

      {hasError && disciplina.validationError && (
        <div
          className={`text-xs font-medium px-3 py-1.5 rounded-lg border ${validationLabels[disciplina.validationError].color}`}
        >
          {validationLabels[disciplina.validationError].text}
        </div>
      )}

      {isReprovada && (
        <div className="text-xs font-semibold text-red-700 bg-red-100 px-3 py-1.5 rounded-lg text-center mt-auto">
          Status: Reprovada
        </div>
      )}

      {isActuallyEnrolled && (
        <div className="text-xs font-semibold text-brand-primary bg-brand-light px-3 py-1.5 rounded-lg text-center mt-auto">
          Status: {statusLabels[disciplina.matriculaStatus!]}
        </div>
      )}

      {!isActuallyEnrolled && !isIndisponivel && (
        <button
          onClick={() => onEnroll(disciplina.id)}
          disabled={loading}
          className="w-full bg-brand-primary text-white font-medium text-sm leading-5 px-4 py-2 rounded-lg hover:bg-indigo-700 active:bg-indigo-800 transition-colors disabled:opacity-50 disabled:cursor-not-allowed mt-auto"
        >
          {loading ? 'Inscrevendo...' : 'Inscrever-se'}
        </button>
      )}

      {isIndisponivel && !isActuallyEnrolled && (
        <div className="text-xs font-medium text-ui-muted bg-gray-100 px-3 py-2 rounded-lg text-center mt-auto">
          Disciplina indisponível no momento
        </div>
      )}

      {showModal && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center p-4"
          onClick={() => setShowModal(false)}
        >
          <div className="fixed inset-0 bg-black/40" />
          <div
            className="relative bg-white rounded-2xl shadow-xl max-w-lg w-full max-h-[90vh] overflow-y-auto p-6 z-10"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="flex items-start justify-between gap-3 mb-4">
              <div>
                <h2 className="text-lg font-bold text-ui-dark">{disciplina.nome}</h2>
                <p className="text-sm text-ui-muted">{disciplina.codigo}</p>
              </div>
              <button
                onClick={() => setShowModal(false)}
                className="text-ui-muted hover:text-ui-dark transition-colors shrink-0"
              >
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none" stroke="currentColor" strokeWidth="1.5">
                  <path d="M5 5l10 10M15 5L5 15" />
                </svg>
              </button>
            </div>

            <div className="space-y-4">
              <div>
                <h3 className="text-xs font-semibold text-ui-muted uppercase tracking-wide mb-1">Descrição</h3>
                <p className="text-sm text-ui-dark leading-relaxed">
                  {disciplina.descricao || 'Nenhuma descrição disponível.'}
                </p>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <h3 className="text-xs font-semibold text-ui-muted uppercase tracking-wide mb-1">Professor</h3>
                  <p className="text-sm text-ui-dark">{disciplina.nomeProfessor}</p>
                </div>
                <div>
                  <h3 className="text-xs font-semibold text-ui-muted uppercase tracking-wide mb-1">Créditos</h3>
                  <p className="text-sm text-ui-dark">{disciplina.creditos}</p>
                </div>
              </div>

              {disciplina.horarios.length > 0 && (
                <div>
                  <h3 className="text-xs font-semibold text-ui-muted uppercase tracking-wide mb-1">Horários</h3>
                  <div className="flex flex-wrap gap-1.5">
                    {disciplina.horarios.map((h, i) => (
                      <span
                        key={i}
                        className="text-xs text-ui-dark bg-ui-bg border border-ui-border px-2.5 py-1 rounded-md font-medium"
                      >
                        {formatHorario(h.diaDaSemana, h.horarioInicio, h.horarioFim)}
                      </span>
                    ))}
                  </div>
                </div>
              )}

              {disciplina.preRequisitos.length > 0 && (
                <div>
                  <h3 className="text-xs font-semibold text-ui-muted uppercase tracking-wide mb-1">Pré-requisitos</h3>
                  <ul className="space-y-1">
                    {disciplina.preRequisitos.map((pre) => (
                      <li key={pre.id} className="text-sm text-ui-dark flex items-center gap-1.5">
                        <span className="w-1.5 h-1.5 rounded-full bg-brand-primary shrink-0" />
                        {pre.codigo} - {pre.nome}
                      </li>
                    ))}
                  </ul>
                </div>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
