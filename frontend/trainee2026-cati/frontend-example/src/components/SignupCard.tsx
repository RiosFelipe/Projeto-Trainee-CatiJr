import { FormEvent, useState } from 'react'
import { GraduationCapIcon, EyeOffIcon } from '../assets/icons'
import InputField from './InputField'
import { Page } from '../types'
import axios from 'axios'

interface SignupCardProps {
  onNavigate?: (page: Page) => void
}

export default function SignupCard({ onNavigate }: SignupCardProps) {
  const [showPassword, setShowPassword] = useState(false)

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()

    // 1. Captura os dados diretamente do formulário usando a API FormData
    const formData = new FormData(e.currentTarget)
    const nome = formData.get('nome')
    const email = formData.get('email')
    const password = formData.get('password')
    const confirmPassword = formData.get('confirmPassword')

    //validação local de senha
    if (password !== confirmPassword) {
      alert('As senhas não coincidem!')
      return
    }

    try {
      //envia pro endereço do usercontroller (@PostMapping)
      await axios.post('http://localhost:8080/aluno', {
        nome,
        email,
        password
      })

      alert('Cadastro realizado com sucesso!')

      //redireciona o aluno para a tela de Login
      onNavigate?.('login')

    } catch (error: any) {
      console.error('Erro no cadastro:', error)

      //trata a resposta enviada 
      if (error.response) {
        //se o backend retornou "email já existe" (HttpStatus.BAD_REQUEST)
        const mensagemDoJava = error.response.data
        alert(mensagemDoJava || 'Erro ao realizar o cadastro.')
      } else if (error.request) {
        alert('Servidor Spring Boot offline ou sem resposta.')
      } else {
        alert('Erro ao processar a requisição.')
      }
    }
  }

  return (
    <div className="bg-white border border-[rgba(199,196,216,0.4)] rounded-2xl shadow-[0px_25px_50px_-12px_rgba(79,70,229,0.05)] flex flex-col gap-8 p-6 sm:p-[41px]">
      <div className="flex flex-col items-center w-full">
        <div className="mb-4">
          <div className="bg-[rgba(79,70,229,0.1)] border border-[rgba(79,70,229,0.1)] flex items-center justify-center w-14 h-14 rounded-xl shadow-sm p-px">
            <GraduationCapIcon width={29} height={24} color="#4f46e5" />
          </div>
        </div>

        <h2 className="font-semibold text-[20px] text-brand-accent tracking-[-0.5px] leading-7 text-center mb-1">
          MatriculaFácil
        </h2>

        <h1 className="font-bold text-[28px] sm:text-[32px] text-ui-dark tracking-[-0.64px] leading-tight text-center mb-2">
          Criar Conta
        </h1>

        <p className="font-normal text-base text-ui-medium leading-6 text-center max-w-[320px]">
          Preencha os dados abaixo para iniciar sua jornada acadêmica.
        </p>
      </div>

      <form onSubmit={handleSubmit} className="flex flex-col gap-5 w-full">
        <InputField
          name="nome"
          label="Nome Completo"
          type="text"
          placeholder="Ex: João da Silva"
        />

        <InputField
          name="email" 
          label="E-mail Institucional"
          type="email"
          placeholder="joao.silva@aluno.edu.br"
        />

        <InputField
          name="password" 
          label="Senha"
          type={showPassword ? 'text' : 'password'}
          placeholder="••••••••"
          rightIcon={
            <button
              type="button"
              onClick={() => setShowPassword((v) => !v)}
              className="flex items-center justify-center"
            >
              <EyeOffIcon />
            </button>
          }
        />

        <div className="pb-2">
          <InputField
            name="confirmPassword" 
            label="Confirmar Senha"
            type="password"
            placeholder="••••••••"
          />
        </div>

        <button
          type="submit"
          className="w-full bg-brand-accent text-white font-normal text-base leading-6 py-[14px] rounded-lg shadow-[0px_4px_6px_-1px_rgba(79,70,229,0.2),0px_2px_4px_-2px_rgba(79,70,229,0.2)] hover:bg-indigo-700 active:bg-indigo-800 transition-colors"
        >
          Criar Conta
        </button>
      </form>

      <div className="pt-2">
        <div className="border-t border-[rgba(199,196,216,0.2)] pt-6 w-full">
          <div className="flex items-center justify-center gap-1">
            <span className="font-normal text-[15px] text-ui-medium leading-[22.5px]">
              Já tem uma conta?
            </span>
            <button
              type="button"
              onClick={() => onNavigate?.('login')}
              className="font-medium text-[15px] text-brand-accent leading-[22.5px] hover:underline"
            >
              Entre
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}