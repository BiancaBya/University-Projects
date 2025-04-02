# Un client trimite unui server un sir de caractere si doua numere (fie acestea s, i, l).
# Serverul va returna clientului subsirul de lungime l a lui s care incepe la pozitia i.

# TCP

import socket

TCP_IP = '127.0.0.1'
TCP_PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((TCP_IP, TCP_PORT))

sir = input("Sirul de caractere: ")
sock.send(sir.encode())
print("Am trimis la server: ", sir)


poz = input("pozitia: ")
sock.send(poz.encode())
print("Am trimis la server: ", poz)


lg = input("Lungime: ")
sock.send(lg.encode())
print("Am trimis la server: ", lg)


rez = sock.recv(10).decode()
sock.close()

print("Am primit de la server: ", rez)


"""

import socket

TCP_IP = '127.0.0.1'
TCP_PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((TCP_IP, TCP_PORT))

sir = input("Sirul de caractere: ")
sock.send(sir.encode())
print("Am trimis la server: ", sir)


rez = sock.recv(10).decode()
sock.close()

print("Am primit de la server: ", rez)

"""
