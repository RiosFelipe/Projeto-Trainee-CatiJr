import { useEffect, useState } from 'react'
import DashboardHeader from '../components/DashboardHeader'
import CatalogHeading from '../components/CatalogHeading'
import DisciplineCard from '../components/DisciplineCard'
import api, { decodeToken } from '../services/api'
import type { Disciplina, Matricula, CatalogDisciplina, User, ValidationError } from '../types'

export default function DashboardPage() {
  const [user, setUser] = useState<User | null>(null)
  const [disciplinas, setDisciplinas] = useState<CatalogDisciplina[]>([])
  const [loading, setLoading] = useState(true)
  const [enrollingId, setEnrollingId] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [searchQuery, setSearchQuery] = useState('')

  useEffect(() => {
    const token = localStorage.getItem('token')
    if (!token) {
      window.location.reload()
      return
    }

    const decoded = decodeToken(token)
    if (!decoded) {
      localStorage.removeItem('token')
      window.location.reload()
      return
    }

    const nome = localStorage.getItem('userName') || ''
    const email = localStorage.getItem('userEmail') || ''
    setUser({ id: decoded.sub, nome, email })

    loadCatalog()
  }, [])

  async function loadCatalog() {
    try {
      setLoading(true)
      setError(null)

      const [discRes, matrRes] = await Promise.all([
        api.get<Disciplina[]>('/disciplina'),
        api.get<Matricula[]>('/matriculas/minhas-disciplinas'),
      ])

      const enrolledMap = new Map<string, Matricula>()
      matrRes.data.forEach((m) => enrolledMap.set(m.disciplinaId, m))

      let totalCreditos = 0
      const enrolledHorarios: { dia: string; inicio: string; fim: string }[] = []

      const enrolledDisciplinas = new Map(discRes.data.map((d) => [d.id, d]))

      matrRes.data.forEach((m) => {
        if (m.status === 'INSCRITA') {
          totalCreditos += m.disciplinaCreditos

          const disc = enrolledDisciplinas.get(m.disciplinaId)
          if (disc) {
            disc.horarios.forEach((h) => {
              enrolledHorarios.push({
                dia: h.diaDaSemana,
                inicio: h.horarioInicio,
                fim: h.horarioFim,
              })
            })
          }
        }
      })

      const completedSet = new Set(
        matrRes.data
          .filter((m) => m.status === 'CONCLUIDA')
          .map((m) => m.disciplinaId)
      )

      const catalog: CatalogDisciplina[] = discRes.data.map((d) => {
        const enrollment = enrolledMap.get(d.id)
        const matriculaStatus = enrollment?.status ?? null

        let validationError: ValidationError | null = null

        if (!enrollment || enrollment.status === 'REPROVADA') {
          const hasPrereqError = d.preRequisitos.some(
            (pre) => !completedSet.has(pre.id)
          )

          const afterEnroll = totalCreditos + d.creditos

          const hasScheduleConflict = d.horarios.some((h) =>
            enrolledHorarios.some(
              (eh) =>
                eh.dia === h.diaDaSemana &&
                eh.inicio < h.horarioFim &&
                h.horarioInicio < eh.fim
            )
          )

          if (hasPrereqError) {
            validationError = 'PREREQUISITO'
          } else if (afterEnroll > 24) {
            validationError = 'CREDITOS'
          } else if (hasScheduleConflict) {
            validationError = 'HORARIO'
          }
        }

        return { ...d, matriculaStatus, matriculaId: enrollment?.id ?? null, validationError }
      })

      setDisciplinas(catalog)
    } catch (err: any) {
      setError('Erro ao carregar o catálogo. Verifique se o servidor está rodando.')
    } finally {
      setLoading(false)
    }
  }

  async function handleEnroll(disciplinaId: string) {
    try {
      setEnrollingId(disciplinaId)
      await api.post('/matriculas', { disciplinaId })
      await loadCatalog()
    } catch (err: any) {
      const msg =
        err.response?.data?.message ||
        err.response?.data ||
        'Erro ao realizar inscrição.'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setEnrollingId(null)
    }
  }

  async function handleCancel(matriculaId: string) {
    if (!confirm('Tem certeza que deseja cancelar esta inscrição?')) return
    try {
      setEnrollingId(matriculaId)
      await api.delete(`/matriculas/${matriculaId}`)
      await loadCatalog()
    } catch (err: any) {
      const msg =
        err.response?.data?.message ||
        err.response?.data ||
        'Erro ao cancelar inscrição.'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setEnrollingId(null)
    }
  }

  if (loading) {
    return (
      <div className="min-h-screen bg-ui-bg flex items-center justify-center">
        <div className="flex flex-col items-center gap-3">
          <div className="w-8 h-8 border-2 border-brand-primary border-t-transparent rounded-full animate-spin" />
          <p className="text-sm text-ui-muted">Carregando catálogo...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-ui-bg">
      <DashboardHeader user={user} />

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 sm:py-10">
        <CatalogHeading semestre="2024.2" />

        <div className="mt-6">
          <div className="relative w-full max-w-md">
            <svg
              className="absolute left-3 top-1/2 -translate-y-1/2 text-ui-muted"
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <circle cx="11" cy="11" r="8" />
              <path d="M21 21l-4.3-4.3" />
            </svg>
            <input
              type="text"
              placeholder="Buscar por nome ou código..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-10 pr-4 py-2.5 bg-white border border-ui-border rounded-lg text-sm text-ui-dark placeholder:text-ui-muted outline-none focus:border-brand-primary transition-colors"
            />
          </div>
        </div>

        {error && (
          <div className="mt-6 p-4 bg-red-50 border border-red-200 rounded-lg text-sm text-red-700">
            {error}
          </div>
        )}

        <div className="mt-8 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
          {disciplinas
            .filter((d) => {
              if (!searchQuery.trim()) return true
              const q = searchQuery.toLowerCase()
              return (
                d.nome.toLowerCase().includes(q) ||
                d.codigo.toLowerCase().includes(q)
              )
            })
            .map((d) => (
              <DisciplineCard
                key={d.id}
                disciplina={d}
                onEnroll={handleEnroll}
                onCancel={handleCancel}
                loading={enrollingId === d.id}
              />
            ))}
        </div>

        {!error && disciplinas.length === 0 && (
          <div className="mt-8 text-center text-ui-muted">
            Nenhuma disciplina disponível no momento.
          </div>
        )}

        {!error &&
          disciplinas.length > 0 &&
          disciplinas.filter((d) => {
            if (!searchQuery.trim()) return true
            const q = searchQuery.toLowerCase()
            return (
              d.nome.toLowerCase().includes(q) ||
              d.codigo.toLowerCase().includes(q)
            )
          }).length === 0 && (
            <div className="mt-8 text-center text-ui-muted">
              Nenhuma disciplina encontrada para "{searchQuery}".
            </div>
          )}
      </main>
    </div>
  )
}
