;Sa se citeasca de la tastatura doua numere (in baza 10) si sa se calculeze produsul lor. Rezultatul inmultirii se va salva in memorie in variabila "rezultat" (definita in segmentul de date).

bits 32
global start   
extern exit             
extern printf
extern scanf
  
import exit msvcrt.dll  
import printf msvcrt.dll  
import scanf msvcrt.dll

segment data use32 class=data
    
    a dd 0
    b dd 0
    rezultat resq 1
    ; citire
    format_citire db "%d", 0
    mesaj_a db "a = ", 0
    mesaj_b db "b = ", 0
    ; afisare
    mesaj_afisare db "Cele doua numere sunt %d si %d si produsul lor este %lld", 0   ; %lld  pentru quadword  l litera nu 1 cifra
    format_verificare db "%d  %d", 0
    
    
segment code use32 class=code
    start:
        
    ; citim a 
    
    ; printf(mesaj)
        push dword mesaj_a
        call [printf]
        add esp, 4*1
    
    ; scnaf(format, a)
        push dword a 
        push dword format_citire
        call [scanf]
        add esp, 4*2
        
        
    ; citire b
    
    ; printf(mesaj)
        push dword mesaj_b
        call [printf]
        add esp, 4*1
        
    ; scanf(format, b)
        push dword b 
        push dword format_citire
        call [scanf]
        add esp, 4*2
        
    ; print(format_verificare, a, b)   verificare
        push dword [b]              
        push dword [a]
        push dword format_verificare
        call [printf]
        add esp, 4*2
        
    ; a*b = EDX:EAX
    
        mov eax, [a]  ; EAX = a 
        mov ebx, [b]  ; EBX = b 
        mul ebx     ; EDX:EAX = a*b
        mov [rezultat + 0], eax 
        mov [rezultat + 4], edx
        
    ; afisare rezultat 
        
    ; printf(mesaj_afisare, a, b, rezultat)
    
        push dword [rezultat + 4]
        push dword [rezultat]
        push dword [b]
        push dword [a]
        push dword mesaj_afisare
        call [printf]
        add esp, 4*5
    
    
push dword 0      
call [exit]      






