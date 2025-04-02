# Un client trimite unui server un numar.
# Serverul va returna clientului un boolean care sa indice daca numarul respective este prim sau nu.
import socket


UDP_IP = "127.0.0.1"
UDP_PORT = 1234

numar = str(input("Dati un numar: "))

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
print("Trimit la server: ", numar)
sock.sendto(numar.encode(), (UDP_IP, UDP_PORT))


data, adresa = sock.recvfrom(1024)
print("Booleanul este: ", True if data == b'1' else False)


