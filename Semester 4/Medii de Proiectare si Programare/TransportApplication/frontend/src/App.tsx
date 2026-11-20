import React from 'react';
import logo from './logo.svg';
import './App.css';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import Login from "./pages/Login";
import MainPage from "./pages/MainPage";
import RequireAuth from "./pages/RequireAuth";
import AddTrip from "./pages/AddTrip";
import UpdateTrip from "./pages/UpdateTrip";

function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Login />} />

          <Route path="/mainpage" element={
            <RequireAuth>
              <MainPage />
            </RequireAuth>
          }/>

            <Route path="/addtrip" element={
                <RequireAuth>
                    <AddTrip/>
                </RequireAuth>
            }/>

            <Route path="/updatetrip/:id" element={
                <RequireAuth>
                    <UpdateTrip />
                </RequireAuth>
            }/>

            <Route path="*" element={
                <Navigate to="/" replace />
            }/>

        </Routes>
      </BrowserRouter>
  );
}

export default App;



