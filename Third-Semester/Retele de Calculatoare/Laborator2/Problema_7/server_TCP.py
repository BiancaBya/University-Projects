# Un client trimite unui server un sir de caractere si doua numere (fie acestea s, i, l).
# Serverul va returna clientului subsirul de lungime l a lui s care incepe la pozitia i.

# TCP

import socket

TCP_IP = "127.0.0.1"
TCP_PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind((TCP_IP, TCP_PORT))
sock.listen(1)                                           # cati clienti se pot conecta simultan la server

while True:

    client_socket, address = sock.accept()
    print("Connection address: ", address)

    sir = client_socket.recv(1024)
    sir = sir.decode()
    print("Am primit de la client : ", sir)

    if not sir:
        break

    poz = client_socket.recv(1024)
    poz = poz.decode()
    print("Am primit de la server: ", poz)

    if not poz:
        break

    lg = client_socket.recv(1024)
    lg = lg.decode()
    print("Am primit de la server: ", lg)

    if not lg:
        break

    rez = sir[int(poz) : int(lg) + int(poz)]
    print("Am trimis la client: ", rez)
    client_socket.send(rez.encode())
    client_socket.close()




"""

import socket

TCP_IP = "127.0.0.1"
TCP_PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind((TCP_IP, TCP_PORT))
sock.listen(1) 

while True:

    client_socket, address = sock.accept()
    print("Connection address: ", address)

    sir = client_socket.recv(1024)
    sir = sir.decode()
    print("Am primit de la client : ", sir)

    if not sir:
        break
        
    rez = ...
    print("Am trimis la client: ", rez)
    client_socket.send(rez.encode())
    client_socket.close()



"""
