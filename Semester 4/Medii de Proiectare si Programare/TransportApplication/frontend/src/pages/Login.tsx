import React, { useState } from 'react';
import styled, { createGlobalStyle } from 'styled-components';
import { useNavigate } from 'react-router-dom';

const GlobalStyle = createGlobalStyle`
  *, *::before, *::after {
    box-sizing: border-box;
  }
  body {
    margin: 0;
    background: linear-gradient(135deg, #3a8dff 0%, #18ffff 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100vh;
  }
`;

const Card = styled.div`
  background: #ffffffee;
  border-radius: 12px;
  padding: 3rem 2rem;
  width: 360px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  text-align: center;
`;

const Title = styled.h1`
  margin-bottom: 1.5rem;
  font-size: 2rem;
  font-weight: 600;
  color: #0d47a1;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const Input = styled.input`
  padding: 0.75rem 1rem;
  font-size: 1rem;
  border: 2px solid #90caf9;
  border-radius: 6px;
  transition: border-color 0.2s;
  &:focus {
    border-color: #1e88e5;
    outline: none;
  }
`;

const Button = styled.button`
  margin-top: 1rem;
  padding: 0.75rem;
  font-size: 1rem;
  font-weight: 600;
  color: #fff;
  background: linear-gradient(135deg, #1e88e5 0%, #42a5f5 100%);
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s;
  &:hover {
    background: linear-gradient(135deg, #1565c0 0%, #1e88e5 100%);
  }
`;

const Footer = styled.p`
  margin-top: 1.5rem;
  font-size: 0.9rem;
  color: #555;
  a {
    color: #1565c0;
    text-decoration: none;
    font-weight: 600;
    margin-left: 4px;
    &:hover {
      text-decoration: underline;
    }
  }
`;

const ErrorText = styled.p`
  color: #c62828;
  font-size: 0.9rem;
  margin: 0;
  text-align: left;
`;

export default function Login() {
    const [email, setEmail]       = useState('');
    const [password, setPassword] = useState('');
    const [error, setError]       = useState<string | null>(null);
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        if (!email || !password) {
            setError('Completează toate câmpurile!');
            return;
        }

        try {
            const url = new URL('http://localhost:8080/api/auth/login');
            url.searchParams.set('email', email);
            url.searchParams.set('password', password);

            const res = await fetch(url.toString(), { method: 'POST' });
            const text = await res.text();

            if (!res.ok) {
                throw new Error(text || 'Eroare la autentificare');
            }

            const { token } = JSON.parse(text);
            sessionStorage.setItem('token', token);
            navigate('/mainpage');
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Eroare necunoscută');
        }
    };

    return (
        <>
            <GlobalStyle />
            <Card>
                <Title>Transport App</Title>
                <Form onSubmit={handleSubmit}>
                    {error && <ErrorText>{error}</ErrorText>}
                    <Input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={e => setEmail(e.target.value)}
                    />
                    <Input
                        type="password"
                        placeholder="Parolă"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                    />
                    <Button type="submit">Log In</Button>
                </Form>
            </Card>
        </>
    );
}


