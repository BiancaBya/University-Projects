# Un client trimite unui server un sir de caractere si doua numere (fie acestea s, i, l).
# Serverul va returna clientului subsirul de lungime l a lui s care incepe la pozitia i.

# UDP concurent

import socket
import threading


def handle_client(socket_client, sir, poz, lg):

    poz = int(poz)
    lg = int(lg)

    rez = sir[int(poz): int(lg) + int(poz)]
    print("Am trimis la client: ", rez)

    socket_client.sendto(rez.encode(), adresa)



UDP_IP = '127.0.0.1'
UDP_PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((UDP_IP, UDP_PORT))

while True:

    data, adresa = sock.recvfrom(1024)

    sir, poz, lg = data.decode().split(',')

    client_thread = threading.Thread(target=handle_client, args=(sock, sir, poz, lg))
    client_thread.start()


