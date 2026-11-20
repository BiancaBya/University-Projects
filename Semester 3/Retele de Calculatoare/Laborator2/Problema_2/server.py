# Un client trimite unui server un numar.
# Serverul va returna clientului un boolean care sa indice daca numarul respective este prim sau nu.

import socket

def prim(n):
    if n == 0 or n == 1:
        return False
    if n == 2:
        return True
    if n % 2 == 0:
        return False
    for d in range(3, n):
        if n % d == 0:
            return False
    return True

UDP_IP = "127.0.0.1"
UDP_PORT = 1234


sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((UDP_IP, UDP_PORT))


while True:
    numar, adresa = sock.recvfrom(1024)
    print("Am primit de la client: ", numar)

    if prim(int(numar)):
        este_prim = True
    else:
        este_prim = False

    print("Trimit la client: ", este_prim)
    sock.sendto(b'1' if este_prim else b'0', adresa)











