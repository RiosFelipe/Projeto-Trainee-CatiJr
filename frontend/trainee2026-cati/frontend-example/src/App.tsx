import { useState } from 'react'
import LoginPage from './pages/LoginPage'
import SignupPage from './pages/SignupPage'
import DashboardPage from './pages/DashboardPage'
import ForgotPasswordPage from './pages/ForgotPasswordPage'
import { Page } from './types'

export default function App() {
  const [page, setPage] = useState<Page>('login')

  if (page === 'signup') return <SignupPage onNavigate={setPage} />
  if (page === 'dashboard') return <DashboardPage />
  if (page === 'forgot-password') return <ForgotPasswordPage onNavigate={setPage} />
  return <LoginPage onNavigate={setPage} />
}
