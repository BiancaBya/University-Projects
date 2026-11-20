# Un client trimite unui server un sir de caractere si doua numere (fie acestea s, i, l).
# Serverul va returna clientului subsirul de lungime l a lui s care incepe la pozitia i.

# UDP

"""
import socket

UDP_IP = '127.0.0.1'
UDP_PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((UDP_IP, UDP_PORT))

while True:

    sir, adresa = sock.recvfrom(1024)
    print("Am primit de la client: ", sir)
    sir = sir.decode()

    nr = ...
    sock.sendto(str(nr).encode(), adresa)

"""


#!!! encode() se da la un str nu la int

import socket

UDP_IP = '127.0.0.1'
UDP_PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((UDP_IP, UDP_PORT))

while True:

    sir, adresa = sock.recvfrom(1024)
    print("Am primit de la adresa: ", adresa, " sirul: ", sir)

    poz, adresa = sock.recvfrom(1024)
    print("Am primit de la adresa: ", adresa, " pozitia: ", poz)

    lg, adresa = sock.recvfrom(1024)
    print("am primit de la adresa: ", adresa, " lungimea: ", lg)

    sir = sir.decode()
    poz = int(poz.decode())
    lg = int(lg.decode())

    rez = sir[int(poz): int(lg) + int(poz)]
    print("Am trimis la client: ", rez)

    sock.sendto(rez.encode(), adresa)







