import { useEffect, useState } from 'react'
import api from '../services/api'
import type { Matricula, Page, User } from '../types'

interface PerfilPageProps {
  onNavigate: (page: Page) => void
}

export default function PerfilPage({ onNavigate }: PerfilPageProps) {
  const [user, setUser] = useState<User | null>(null)
  const [createdAt, setCreatedAt] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [stats, setStats] = useState({ inscritas: 0, concluidas: 0, reprovadas: 0, creditos: 0 })

  useEffect(() => {
    loadPerfil()
  }, [])

  async function loadPerfil() {
    try {
      setLoading(true)
      setError(null)

      const token = localStorage.getItem('token')
      if (!token) {
        onNavigate('login')
        return
      }

      const [userRes, matrRes] = await Promise.all([
        api.get<{ id: string; nome: string; email: string; createdAt: string }>('/aluno/me'),
        api.get<Matricula[]>('/matriculas/minhas-disciplinas'),
      ])

      const userData = userRes.data
      setUser({ id: userData.id, nome: userData.nome, email: userData.email })
      setCreatedAt(userData.createdAt)

      const mat = matrRes.data
      setStats({
        inscritas: mat.filter((m) => m.status === 'INSCRITA').length,
        concluidas: mat.filter((m) => m.status === 'CONCLUIDA').length,
        reprovadas: mat.filter((m) => m.status === 'REPROVADA').length,
        creditos: mat
          .filter((m) => m.status === 'INSCRITA')
          .reduce((sum, m) => sum + m.disciplinaCreditos, 0),
      })
    } catch (err: any) {
      setError('Erro ao carregar perfil.')
    } finally {
      setLoading(false)
    }
  }

  function formatDate(iso: string) {
    const d = new Date(iso)
    return d.toLocaleDateString('pt-BR', { day: '2-digit', month: 'long', year: 'numeric' })
  }

  if (loading) {
    return (
      <div className="min-h-screen bg-ui-bg flex items-center justify-center">
        <div className="flex flex-col items-center gap-3">
          <div className="w-8 h-8 border-2 border-brand-primary border-t-transparent rounded-full animate-spin" />
          <p className="text-sm text-ui-muted">Carregando perfil...</p>
        </div>
      </div>
    )
  }

  if (error || !user) {
    return (
      <div className="min-h-screen bg-ui-bg flex items-center justify-center">
        <p className="text-red-600">{error || 'Usuário não encontrado.'}</p>
      </div>
    )
  }

  const initials = user.nome
    .split(' ')
    .filter((_, i, arr) => i === 0 || i === arr.length - 1)
    .map((n) => n[0].toUpperCase())
    .join('')

  return (
    <div className="min-h-screen bg-ui-bg">
      <header className="bg-white border-b border-ui-border">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 flex items-center justify-between h-16">
          <button
            onClick={() => onNavigate('dashboard')}
            className="flex items-center gap-2 text-sm font-medium text-ui-muted hover:text-ui-dark transition-colors"
          >
            <svg width="20" height="20" viewBox="0 0 20 20" fill="none" stroke="currentColor" strokeWidth="1.5">
              <path d="M12 5l-5 5 5 5" />
            </svg>
            Voltar
          </button>
          <h1 className="font-bold text-lg text-ui-dark">Meu Perfil</h1>
          <div className="w-[52px]" />
        </div>
      </header>

      <main className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8 sm:py-10 space-y-6">
        <div className="bg-white border border-ui-border rounded-xl drop-shadow-sm p-6 sm:p-8">
          <div className="flex items-center gap-4 mb-6">
            <div className="w-16 h-16 rounded-full bg-brand-accent flex items-center justify-center shrink-0">
              <span className="text-white text-xl font-bold">{initials}</span>
            </div>
            <div>
              <h2 className="text-xl font-bold text-ui-dark">{user.nome}</h2>
              <p className="text-sm text-ui-muted">{user.email}</p>
            </div>
          </div>

          {createdAt && (
            <div className="flex items-center gap-2 text-sm text-ui-muted border-t border-ui-border pt-4">
              <svg width="16" height="16" viewBox="0 0 20 20" fill="none" stroke="currentColor" strokeWidth="1.5">
                <rect x="2" y="4" width="16" height="14" rx="2" />
                <path d="M2 8h16M6 2v4M14 2v4" />
              </svg>
              <span>Membro desde {formatDate(createdAt)}</span>
            </div>
          )}
        </div>

        <div className="bg-white border border-ui-border rounded-xl drop-shadow-sm p-6 sm:p-8">
          <h3 className="font-semibold text-base text-ui-dark mb-4">Resumo Acadêmico</h3>
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
            <div className="bg-brand-light rounded-lg p-4 text-center">
              <p className="text-2xl font-bold text-brand-primary">{stats.inscritas}</p>
              <p className="text-xs text-ui-muted mt-1">Inscritas</p>
            </div>
            <div className="bg-green-50 rounded-lg p-4 text-center">
              <p className="text-2xl font-bold text-green-700">{stats.concluidas}</p>
              <p className="text-xs text-ui-muted mt-1">Concluídas</p>
            </div>
            <div className="bg-red-50 rounded-lg p-4 text-center">
              <p className="text-2xl font-bold text-red-700">{stats.reprovadas}</p>
              <p className="text-xs text-ui-muted mt-1">Reprovadas</p>
            </div>
            <div className="bg-orange-50 rounded-lg p-4 text-center">
              <p className="text-2xl font-bold text-orange-700">{stats.creditos}/24</p>
              <p className="text-xs text-ui-muted mt-1">Créditos</p>
            </div>
          </div>
        </div>

      </main>
    </div>
  )
}
