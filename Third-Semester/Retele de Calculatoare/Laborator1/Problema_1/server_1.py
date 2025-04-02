# Un client trimite unui server un sir de numere.
# Serverul va returna clientului suma numerelor primite.

import socket

IP = "172.30.243.238"
PORT = 4444

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((IP, PORT))  # leaga serverul de adresa prin IP si PORT
s.listen(1)  # serverul primeste date de la clienti, parametrul 1 -> un singur client poate face conexiunea

while 1:

    suma = 0 # reinitializam suma cu 0

    sock, address = s.accept()  # blocheaza executia programului
                                # pana cand un client se conecteaza la server
    print("Connection Address : ", address)
    # sock = socket-ul folosit pentru a comunica cu clientul
    # address = touple (IP, PORT)
    numere = ""
    data = sock.recv(1).decode()
    while data != '-':
        numere += data
        data = sock.recv(1).decode()

    numere = numere.split(' ')
    if '' in numere:
        numere.remove('')

    print(numere)
    for nr in numere:
        print("Am primit de la client", nr)
        print("Suma ", suma)
        suma += int(nr)

    print("Am trimis la client ", suma)
    sock.send(str(suma).encode())

sock.close()


