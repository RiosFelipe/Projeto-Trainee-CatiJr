import { FormEvent, useState } from 'react'
import { GraduationCapIcon } from '../assets/icons'
import InputField from './InputField'
import { Page } from '../types'
import axios from 'axios'

interface ForgotPasswordCardProps {
  onNavigate?: (page: Page) => void
}

export default function ForgotPasswordCard({ onNavigate }: ForgotPasswordCardProps) {
  const [step, setStep] = useState<1 | 2>(1)
  const [emailSaved, setEmailSaved] = useState('')

  async function handleStep1(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const formData = new FormData(e.currentTarget)
    const email = formData.get('email') as string

    if (!email) return

    setEmailSaved(email)
    setStep(2)
    alert('Código de verificação de teste enviado!')
  }

  async function handleStep2(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const formData = new FormData(e.currentTarget)
    const code = formData.get('code')
    const newPassword = formData.get('newPassword')

    try {
      const response = await axios.post('http://localhost:8080/aluno/esqueci-senha', {
        email: emailSaved,
        code,
        newPassword
      })

      alert(response.data)
      onNavigate?.('login')

    } catch (error: any) {
      if (error.response) {
        alert(error.response.data)
      } else {
        alert('Erro ao redefinir a senha.')
      }
    }
  }

  return (
    <div className="bg-white border border-[rgba(199,196,216,0.4)] rounded-2xl shadow-sm flex flex-col gap-6 p-6 sm:p-[41px]">
      <div className="flex flex-col items-center w-full">
        <div className="bg-[rgba(79,70,229,0.1)] border border-[rgba(79,70,229,0.1)] flex items-center justify-center w-12 h-12 rounded-xl mb-2">
          <GraduationCapIcon />
        </div>
        
        <h1 className="font-bold text-2xl text-ui-dark mt-2">
          {step === 1 ? 'Recuperar Senha' : 'Digite o Código'}
        </h1>
        <p className="text-sm text-ui-medium text-center">
          {step === 1
            ? 'Digite seu e-mail para receber o código.'
            : `Digite o código enviado para ${emailSaved} e sua nova senha.`}
        </p>
      </div>

      {step === 1 ? (
        <form onSubmit={handleStep1} className="flex flex-col gap-4">
          <InputField
            name="email"
            label="E-mail Institucional"
            type="email"
            placeholder="seu@email.com"
          />
          <button type="submit" className="w-full bg-brand-accent text-white py-3 rounded-lg hover:bg-indigo-700 transition-colors">
            Continuar
          </button>
        </form>
      ) : (
        <form onSubmit={handleStep2} className="flex flex-col gap-4">
          <InputField
            name="code"
            label="Código de Verificação"
            type="text"
            placeholder="••••"
          />
          <InputField
            name="newPassword"
            label="Nova Senha"
            type="password"
            placeholder="••••••••"
          />
          <button type="submit" className="w-full bg-brand-accent text-white py-3 rounded-lg hover:bg-indigo-700 transition-colors">
            Alterar Senha
          </button>
        </form>
      )}

      <button
        type="button"
        onClick={() => onNavigate?.('login')}
        className="text-sm text-brand-accent hover:underline text-center"
      >
        Voltar para o Login
      </button>
    </div>
  )
}