// src/pages/AddTrip.tsx
import React, { useState } from 'react';
import styled, { createGlobalStyle } from 'styled-components';
import { useNavigate } from 'react-router-dom';

const GlobalStyle = createGlobalStyle`
  *, *::before, *::after { box-sizing: border-box; }
  body {
    margin: 0;
    background: linear-gradient(135deg, #3a8dff 0%, #18ffff 100%);
    min-height: 100vh;
    font-family: sans-serif;
  }
`;

const Container = styled.div`
  max-width: 400px;
  margin: 4rem auto;
  background: #ffffffee;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
`;

const Title = styled.h1`
  font-size: 1.8rem;
  color: #0d47a1;
  text-align: center;
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
  &:focus {
    border-color: #1e88e5;
    outline: none;
  }
`;

const ButtonGroup = styled.div`
  display: flex;
  justify-content: space-between;
`;

const Button = styled.button<{ variant?: 'cancel' }>`
  flex: 1;
  padding: 0.75rem;
  font-size: 1rem;
  color: #fff;
  background: ${({ variant }) =>
    variant === 'cancel'
        ? 'linear-gradient(135deg, #b0bec5 0%, #90a4ae 100%)'
        : 'linear-gradient(135deg, #1e88e5 0%, #42a5f5 100%)'};
  border: none;
  border-radius: 6px;
  cursor: pointer;
  &:hover {
    background: ${({ variant }) =>
    variant === 'cancel'
        ? '#78909c'
        : 'linear-gradient(135deg, #1565c0 0%, #1e88e5 100%)'};
  }
`;

export default function AddTrip() {
    const [destination, setDestination] = useState('');
    const [date, setDate] = useState('');
    const [nrSeats, setNrSeats] = useState(0);
    const navigate = useNavigate();

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        fetch('http://localhost:8080/api/trips', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ destination, date, nrSeats }),
        })
            .then(res => {
                if (res.status === 201) navigate('/mainpage');
                else return Promise.reject();
            })
            .catch(() => alert('Eroare la adăugare cursă'));
    };

    return (
        <>
            <GlobalStyle />
            <Container>
                <Title>Add New Trip</Title>
                <Form onSubmit={handleSubmit}>
                    <Input
                        type="text"
                        placeholder="Destinație"
                        value={destination}
                        onChange={e => setDestination(e.target.value)}
                        required
                    />
                    <Input
                        type="datetime-local"
                        placeholder="Dată și oră"
                        value={date}
                        onChange={e => setDate(e.target.value)}
                        required
                    />
                    <Input
                        type="number"
                        placeholder="Număr locuri"
                        value={nrSeats}
                        onChange={e => setNrSeats(+e.target.value)}
                        min={1}
                        required
                    />
                    <ButtonGroup>
                        <Button type="button" variant="cancel" onClick={() => navigate('/mainpage')}>
                            Cancel
                        </Button>
                        <Button type="submit">Add</Button>
                    </ButtonGroup>
                </Form>
            </Container>
        </>
    );
}
