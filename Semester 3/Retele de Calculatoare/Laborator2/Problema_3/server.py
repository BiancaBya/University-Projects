# Un client trimite unui server un sir de lugime cel mult 100 de caractere.
# Serverul va returna clientului numarul de caractere spatiu din sir.
import socket

UDP_IP = '127.0.0.1'
UDP_PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((UDP_IP, UDP_PORT))

## !!!! la str se da encode si decode nu la int
while True:
    sir, adresa = sock.recvfrom(1024)
    print("Am primit de la client: ", sir)
    nr = len(str(sir).split(" ")) - 1
    sock.sendto(str(nr).encode(), adresa)



