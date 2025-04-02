# Un client trimite unui server un sir de lugime cel mult 100 de caractere.
# Serverul va returna clientului numarul de caractere spatiu din sir.
import socket

UDP_IP = '127.0.0.1'
UDP_PORT = 1234

sir = input("Dati sirul de caractere: ")

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
print("Trimit la server: ", sir)
sock.sendto(sir.encode(), (UDP_IP, UDP_PORT))


data, adresa = sock.recvfrom(1024)
print("Numarul de spatii este: ", data.decode())


