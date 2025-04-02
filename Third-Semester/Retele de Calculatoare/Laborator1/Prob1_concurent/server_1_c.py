import socket
import threading


def handle_client(client_socket):
    suma = 0  # reinitializam suma cu 0

    numere = ""
    data = client_socket.recv(1).decode()
    while data != '-':
        numere += data
        data = client_socket.recv(1).decode()

    numere = numere.split(' ')
    if '' in numere:
        numere.remove('')

    print(numere)
    for nr in numere:
        print("Am primit de la client", nr)
        print("Suma ", suma)
        suma += int(nr)

    print("Am trimis la client ", suma)
    client_socket.send(str(suma).encode())
    client_socket.close()


IP = "172.30.243.238"
PORT = 4444


s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((IP, PORT))  # leaga serverul de adresa prin IP si PORT
s.listen(1)  # serverul primeste date de la clienti, parametrul 1 -> un singur client poate face conexiunea


while 1:

    sock, address = s.accept()  # blocheaza executia programului
                                # pana cand un client se conecteaza la server
    print("Connection Address : ", address)
    # sock = socket-ul folosit pentru a comunica cu clientul
    # address = touple (IP, PORT)
    client_handler = threading.Thread(target=handle_client, args=(sock,))
    client_handler.start()
    
