# Un client trimite unui server un sir de caractere si doua numere (fie acestea s, i, l).
# Serverul va returna clientului subsirul de lungime l a lui s care incepe la pozitia i.

# client trimite la server prin TCP si serverul trimite la client prin UDP

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

    udp_port = int(socket_client.recv(1024).decode())

    rez = sir[int(poz): int(lg) + int(poz)]
    print("Am trimis la client: ", rez)

    sock_UDP = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock_UDP.sendto(rez.encode(), ("127.0.0.1", udp_port))

    socket_client.close()


TCP_IP = "127.0.0.1"
TCP_PORT = 1234

sock_TCP = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock_TCP.bind((TCP_IP, TCP_PORT))
sock_TCP.listen(5)

while True:

    client_sock, address = sock_TCP.accept()
    print("Connection address: ", address)

    client_thread = threading.Thread(target=handle_client, args=(client_sock,))
    client_thread.start()

