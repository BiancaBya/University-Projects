; 27. Se da un fisier text. Fisierul contine numere (in baza 10) separate prin spatii. Sa se citeasca continutul acestui fisier, sa se determine minimul ; numerelor citite si sa se scrie rezultatul la sfarsitul fisierului.

bits 32

global start
extern exit, fopen, fclose 
extern fprintf             
extern fread               

import fread msvcrt.dll 
import fprintf msvcrt.dll   ;!!!!!
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll

segment data use32 class = data 
    
    ;fisier = nume, mod_acces, descriptor      
    nume_fisier db "fisier.txt", 0
    mod_citire db "r", 0              ; pentru citit 
    mod_scriere db "w", 0             ; pentru suprascris
    mod_append db "a", 0              ; ca sa adaugi ceva la finalul unui fisier 
    descriptor_fisier dd -1           ; descriptorul se initilaizeaza cu -1
    
    ; textul citit 
    len equ 100
    text times len db 0    ; variabila in care se va salva continutul fisierului 
    
    minim resb 1
    
segment code use32 class = code
    start:
        
    ; deschidem fisierul  PRIMA DATA pentru citit continutul
    ; eax = fopen(nume_fisier, mod_scriere)         1. nume  2 mod_acces
        push dword mod_citire       
        push dword nume_fisier
        call [fopen]
        add esp, 4*2
    ; in eax se afla valoarea returnata de functia fopen, adica descriptorul fisierului 
    
        mov [descriptor_fisier], eax 
        
        cmp eax, 0
        je final     ; daca fisierul nu a fost deschis corect
    
    
    ; citim continutul fisierului 
    ; fread(text, 1, len, descriptor_fisier)      text = acolo se pune ce e citit,   1 = ce dimensiune are ce citesti, 1 = un octet
    ;                                             len = cat citeste,       descriptor_fisier = descriptorul fisierului 
        push dword [descriptor_fisier]
        push dword len 
        push dword 1
        push dword text 
        call [fread]
        add esp, 4*4
    
    ; dupa apelul functieti EAX contine NUMARUL DE CARACTERE CITITE DIN FISIER 
    
    
    ; parcurgem text byte cu byte 
        
        mov esi, text 
        mov ecx, eax ; lungimea de caractere din fisier 
        lodsb ; al = primul element din sir 
        mov [minim], al ; minimul e la inceput primul element din sir 
        dec ecx ; un element a fost verificat
        
        parcurgere:
            
            lodsb    ; al = elementul curent din sirul text 
            cmp al, ' '
            je peste 
            cmp al, [minim]
            ja peste ; daca al e mai mare minim ramane acelasi
            mov [minim], al 
        
        peste:
        
        loop parcurgere 
    

    
    ; inchidem fisierul 
    ; fclose(descriptor_fisier)                    1. descriptor_fisier 
        push dword [descriptor_fisier]
        call [fclose]
        add esp, 4*1

        
        

        
    ; deschidem fisierul A DOUA OARA pentru a scrie la final, cu modul append
    ; eax = fopen(nume_fisier, mod_append)
        push dword mod_append
        push dword nume_fisier
        call [fopen]
        add esp, 4*2
        
        
      mov [descriptor_fisier], eax 
        
        cmp eax, 0
        je final     ; daca fisierul nu a fost deschis corect  
        
    ; scriem la finalul fisierului minimul 
    ; fprintf(descriptor_fisier, minim)          1. descriptor       2. textul scris
        push dword minim  
        push dword [descriptor_fisier]
        call [fprintf]
        add esp, 4*2
        
        
    ; fclose(descriptor_fisier)
        push dword [descriptor_fisier]
        call [fclose]
        add esp, 4*1



    
        
    ; cum SCRIEM ceva in fisier, trebuie deschis in modul "r" sau "a" 
    ; fprinf(descriptor_fisier, text)             1. descriptor    2. text 
        ;push dword text 
        ;push dword [descriptor_fisier]
        ;call [fprintf]
        ;add esp, 4*2
      
    final:
    
    
push dword 0
call [exit]



;4 5 6 2 3 7 8 10



