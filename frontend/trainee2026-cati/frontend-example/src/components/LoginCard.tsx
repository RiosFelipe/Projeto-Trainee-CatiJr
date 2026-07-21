import { FormEvent } from 'react'
import { GraduationCapIcon, EmailIcon, LockIcon, ArrowRightIcon } from '../assets/icons'
import InputField from './InputField'
import { Page } from '../types'
import axios from 'axios'

interface LoginCardProps {
  onNavigate?: (page: Page) => void
}

export default function LoginCard({ onNavigate }: LoginCardProps) {
  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
  e.preventDefault();

  //captura os dados do formulário de forma simples e direta
  const formData = new FormData(e.currentTarget);
  const email = formData.get('email');
  const password = formData.get('password');

  try {
    //faz a chamada POST com o axios
    //o axios já transforma o objeto { email, password } em JSON automaticamente
    const response = await axios.post('http://localhost:8080/aluno/login', {
      email,
      password
    });

    //a resposta de sucesso traz os dados em .data
    const token = response.data; //token JWT retornado pelo backend

    //salva o token no localStorage
    localStorage.setItem('token', token);

    //vai pro dashboard
    onNavigate?.('dashboard');

  } catch (error: any) {
    console.error('Erro na autenticação:', error);

    //tratamento de erro com o axios
    if (error.response) {
      //o servidor respondeu com um status fora do range 2xx (ex: 401 Unauthorized)
      const errorMessage = error.response.data;
      alert(errorMessage || 'E-mail ou senha incorretos.');
    } else if (error.request) {
      //o requisição foi feita mas não houve resposta (Servidor backend offline)
      alert('Sem resposta do servidor. O seu backend Spring Boot está rodando?');
    } else {
      //algum outro erro de configuração ocorreu ao disparar a requisição
      alert('Erro ao processar a requisição de login.');
    }
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
            className="w-full flex items-center justify-center gap-2 bg-brand-primary text-white font-medium text-sm leading-5 px-4 py-2 rounded-lg hover:bg-indigo-700 active:bg-indigo-800 transition-colors"
          >
            Entrar
            <ArrowRightIcon />
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