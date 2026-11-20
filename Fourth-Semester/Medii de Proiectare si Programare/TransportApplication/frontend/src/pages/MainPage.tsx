import React, { useEffect, useState } from 'react';
import styled, { createGlobalStyle } from 'styled-components';
import { useNavigate } from 'react-router-dom';

interface Trip {
    id: number;
    destination: string;
    date: string;
    nrSeats: number;
}

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
  max-width: 800px;
  margin: 2rem auto;
  background: #ffffffee;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const Title = styled.h1`
  font-size: 2rem;
  color: #0d47a1;
`;

const ActionButton = styled.button`
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, #1e88e5 0%, #42a5f5 100%);
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  &:hover {
    background: linear-gradient(135deg, #1565c0 0%, #1e88e5 100%);
  }
`;

const TableWrapper = styled.div`
  max-height: 400px;
  overflow-y: auto;
  margin-top: 1rem;
`;

const StyledTable = styled.table`
  width: 100%;
  border-collapse: collapse;
  th, td {
    padding: 0.75rem;
    border-bottom: 1px solid #ddd;
    text-align: left;
  }
  th {
    background: #90caf9;
    position: sticky; top: 0;
  }
`;

const RowButton = styled.button`
  margin-right: 0.5rem;
  padding: 0.25rem 0.5rem;
  background: #ef5350;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  &:hover { background: #e53935; }
`;


export default function MainPage() {
    const [trips, setTrips] = useState<Trip[]>([]);
    const navigate = useNavigate();

    const handleLogout = () => {
        sessionStorage.removeItem('token');
        navigate('/');
    };

    const fetchTrips = () => {
        fetch('http://localhost:8080/api/trips')
            .then(res => res.ok ? res.json() : Promise.reject())
            .then((data: Trip[]) => setTrips(data))
            .catch(console.error);
    };

    useEffect(fetchTrips, []);

    const handleDelete = (id: number) => {
        fetch(`http://localhost:8080/api/trips/${id}`, { method: 'DELETE' })
            .then(res => {
                if (res.status === 204) setTrips(trips.filter(t => t.id !== id));
                else console.error('Stergere esuata');
            })
            .catch(console.error);
    };

    return (
        <>
            <GlobalStyle />
            <Container>
                <Header>
                    <Title>Lista curselor</Title>
                    <ActionButton onClick={() => navigate('/addtrip')}>
                        Add Trip
                    </ActionButton>
                    <ActionButton onClick={handleLogout} style={{ marginLeft: '0.5rem', background: '#ef5350' }}>
                        Logout
                    </ActionButton>
                </Header>
                <TableWrapper>
                    <StyledTable>
                        <thead>
                        <tr>
                            <th>Destinație</th>
                            <th>Dată</th>
                            <th>Locuri disponibile</th>
                            <th>Acțiuni</th>
                        </tr>
                        </thead>
                        <tbody>
                        {trips.map(t => (
                            <tr key={t.id}>
                                <td>{t.destination}</td>
                                <td>{new Date(t.date).toLocaleString('ro-RO')}</td>
                                <td>{t.nrSeats}</td>
                                <td>
                                    <RowButton onClick={() => navigate(`/updatetrip/${t.id}`)}>
                                        Edit
                                    </RowButton>
                                    <RowButton onClick={() => handleDelete(t.id)}>
                                        Delete
                                    </RowButton>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </StyledTable>
                </TableWrapper>
            </Container>
        </>
    );
}



