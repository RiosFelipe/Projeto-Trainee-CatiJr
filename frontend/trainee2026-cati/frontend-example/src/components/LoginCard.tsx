import { FormEvent, useState } from 'react'
import { GraduationCapIcon, EmailIcon, LockIcon, ArrowRightIcon } from '../assets/icons'
import InputField from './InputField'
import { Page } from '../types'
import api from '../services/api'

interface LoginCardProps {
  onNavigate?: (page: Page) => void
}

export default function LoginCard({ onNavigate }: LoginCardProps) {
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()

    const formData = new FormData(e.currentTarget)
    const email = formData.get('email') as string
    const password = formData.get('password') as string

    try {
      setLoading(true)

      const response = await api.post('/aluno/login', { email, password })

      const token = response.data
      localStorage.setItem('token', token)
      localStorage.setItem('userEmail', email)

      onNavigate?.('dashboard')
    } catch (error: any) {
      if (error.response) {
        const errorMessage = error.response.data
        alert(errorMessage || 'E-mail ou senha incorretos.')
      } else if (error.request) {
        alert('Sem resposta do servidor. O seu backend Spring Boot está rodando?')
      } else {
        alert('Erro ao processar a requisição de login.')
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="bg-white border border-ui-border rounded-xl drop-shadow-[0px_1px_1px_rgba(0,0,0,0.05)] flex flex-col gap-8 p-6 sm:p-[33px]">
      <div className="flex flex-col items-center gap-1 w-full">
        <div className="bg-brand-light flex items-center justify-center w-12 py-[10px] rounded-xl">
          <GraduationCapIcon />
        </div>

        <div className="flex flex-col items-center w-full pt-3">
          <h1 className="font-bold text-[30px] text-brand-primary tracking-[-0.6px] leading-[38px] text-center w-full">
            MatriculaFácil
          </h1>
          <p className="text-base text-ui-medium leading-6 text-center">
            Acesse o Portal do Aluno
          </p>
        </div>
      </div>

      <form className="flex flex-col gap-6 w-full" onSubmit={handleSubmit}>
        <InputField
          name="email"
          label="E-mail"
          icon={<EmailIcon />}
          type="email"
          placeholder="seu@email.com"
        />

        <InputField
          name="password"
          label="Senha"
          icon={<LockIcon />}
          type="password"
          placeholder="••••••••"
          rightElement={
            <button
              type="button"
              onClick={() => onNavigate?.('forgot-password')}
              className="hover:underline outline-none"
            >
              Esqueceu a senha?
            </button>
          }
        />

        <div className="pt-2">
          <button
            type="submit"
            disabled={loading}
            className="w-full flex items-center justify-center gap-2 bg-brand-primary text-white font-medium text-sm leading-5 px-4 py-2 rounded-lg hover:bg-indigo-700 active:bg-indigo-800 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? 'Entrando...' : 'Entrar'}
            {!loading && <ArrowRightIcon />}
          </button>
        </div>
      </form>

      <div className="border-t border-ui-border w-full pt-[25px]">
        <div className="flex items-center justify-center gap-1">
          <span className="text-base text-ui-medium leading-6">
            Não tem uma conta?
          </span>
          <button
            type="button"
            onClick={() => onNavigate?.('signup')}
            className="font-medium text-sm text-brand-primary leading-5 hover:underline"
          >
            Cadastre-se
          </button>
        </div>
      </div>
    </div>
  )
}