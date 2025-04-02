# Un client trimite unui server un sir de caractere si doua numere (fie acestea s, i, l).
# Serverul va returna clientului subsirul de lungime l a lui s care incepe la pozitia i.

# UDP concurent

import socket

UDP_IP = '127.0.0.1'
UDP_PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

sir = input("Sirul de caractere: ")
poz = input("pozitia: ")
lg = input("Lungime: ")

mesaj = f"{sir},{poz},{lg}"
print("Trimit la server: ", mesaj)

sock.sendto(mesaj.encode(), (UDP_IP, UDP_PORT))


data, adresa = sock.recvfrom(1024)
print("Am primit de la adresa: ", adresa, " data: ", data.decode())


