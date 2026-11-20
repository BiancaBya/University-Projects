# Un client trimite unui server un sir de caractere si doua numere (fie acestea s, i, l).
# Serverul va returna clientului subsirul de lungime l a lui s care incepe la pozitia i.

# UDP

import socket

UDP_IP = '127.0.0.1'
UDP_PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

sir = input("Sirul de caractere: ")
print("Trimit la server: ", sir)
sock.sendto(sir.encode(), (UDP_IP, UDP_PORT))


poz = input("pozitia: ")
print("Trimit la server: ", poz)
sock.sendto(poz.encode(), (UDP_IP, UDP_PORT))

lg = input("Lungime: ")
print("Trimit la server: ", lg)
sock.sendto(lg.encode(), (UDP_IP, UDP_PORT))


data, adresa = sock.recvfrom(1024)
print("Am primit de la adresa: ", adresa, " data: ", data.decode())



"""

import socket

UDP_IP = '127.0.0.1'
UDP_PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

sir = input("Sirul de caractere: ")
print("Trimit la server: ", sir)
sock.sendto(sir.encode(),(UDP_IP, UDP_PORT))


data, adresa = sock.recvfrom(1024)
print("Am primit de la adresa: ", adresa, " data: ", data.decode())

"""

