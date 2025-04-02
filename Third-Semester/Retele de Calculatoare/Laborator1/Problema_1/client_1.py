# Un client trimite unui server un sir de numere.
# Serverul va returna clientului suma numerelor primite.

import socket

IP = "172.30.243.238"
PORT = 4444
print("Sirul de numere, dati -1 pentru terminare:")

numere = []
numar = int(input("Numar: "))
while numar != -1:
    numere.append(numar)
    numar = int(input("Numar: "))

numere.append(-1) # conditia de oprire

# AF_INET = familia de adrese utilizate
# SOCK_STREAM =  protocolul -> se foloseste TCP
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((IP, PORT))  # tuple cu cele doua argumente

for nr in numere:
    s.send(str(nr).encode())  # ca sa se trimia bytes nu int
    s.send(' '.encode())
    print("Am trimis la server", nr)


suma = s.recv(10).decode()
s.close()

print("Am primit de la server", suma)

