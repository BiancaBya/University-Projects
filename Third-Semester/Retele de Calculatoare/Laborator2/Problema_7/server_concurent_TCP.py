# Un client trimite unui server un sir de caractere si doua numere (fie acestea s, i, l).
# Serverul va returna clientului subsirul de lungime l a lui s care incepe la pozitia i.

# TCP concurent

import socket
import threading


def handle_client(socket_client):

    sir = socket_client.recv(1024)
    sir = sir.decode()
    print("Am primit de la client : ", sir)

    poz = socket_client.recv(1024)
    poz = poz.decode()
    print("Am primit de la server: ", poz)

    lg = socket_client.recv(1024)
    lg = lg.decode()
    print("Am primit de la server: ", lg)

    rez = sir[int(poz): int(lg) + int(poz)]
    print("Am trimis la client: ", rez)
    socket_client.send(rez.encode())

    socket_client.close()


TCP_IP = "127.0.0.1"
TCP_PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind((TCP_IP, TCP_PORT))
sock.listen(5)

while True:

    client_sock, address = sock.accept()
    print("Connection address: ", address)

    client_thread = threading.Thread(target=handle_client, args=(client_sock,))
    client_thread.start()

