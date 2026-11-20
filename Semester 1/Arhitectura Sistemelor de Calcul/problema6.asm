;2.	Se citește de la tastatură un număr N și apoi N numere.
;Să se calculeze suma numerelor pare introduse, să se calculeze suma numerelor impare introduse, apoi diferența dintre cele două sume.
;Să se afișeze în fișierul output.txt cele 3 rezultate, în baza 16, pe linii separate.


bits 32

global start
extern exit, fopen, fclose, fprintf, fread, printf, scanf

import exit msvcrt.dll
import fopen msvcrt.dll 
import fclose msvcrt.dll
import fprintf msvcrt.dll
import fread msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll 


segment data use32 class = data 
    
    n dd 0
    numar dd 0
    suma_pare dd 0
    suma_impare dd 0
    diferenta dd 0
    
    format db "%d", 0
    mesaj_n db "n = ", 0
    mesaj_numere db "Introduceti numerele:", 13, 10, 0
    format_afisare db "Suma numerelor pare este %d si cea a numerelor impare este %d si diferenta este %d", 13, 10, 0
    format_hexa db "Suma numerelor pare hexa este %x si cea a numerelor impare hexa este %x si diferenta hexa este %x", 13, 10, 0
    format_hexa_fisier db "%x", 13, 10, 0
    
    nume_fisier db "fisierul4", 0
    descriptor_fisier dd -1
    mod_acces db "w", 0

segment code use32 class = code 
    start:
    
    ; printf(mesaj_n)
        push dword mesaj_n
        call [printf]
        add esp, 4*1
        
    ; scanf(format, n)
        push dword n
        push dword format 
        call [scanf]
        add esp, 4*2
    
    ; printf(mesaj_numere)
        push dword mesaj_numere
        call [printf]
        add esp, 4*1
    
    ; citim cele n numere 
        
        mov ecx, [n]
    citire:
        push ecx
    ; scanf(format, numar)
        push dword numar
        push dword format
        call [scanf]
        add esp, 4*2
        
        ; IMPARTIREA DWORD la 2 => DX:AX : reg16 -> restul in DX , catul => AX
        mov bx, 2
        mov ax, [numar]        ; AX = [var]        !!
        mov dx, [numar+2]      ; DX = [var + 2]    !!
        div bx 
        ; dupa impartire restul se afla in edx
        
        cmp edx, 0
        je par 
        mov eax, [numar]
        mov ebx, [suma_impare]
        add eax, ebx
        mov [suma_impare], eax
        
        jmp peste
        
    par:
        
        mov eax, [numar]
        mov ebx, [suma_pare]
        add eax, ebx
        mov [suma_pare], eax 
        
    peste:
    
        pop ecx
        
    loop citire 
    
        mov eax, [suma_pare]
        mov ebx, [suma_impare]
        sub eax, ebx 
        cmp eax, 0
        ; facem diferenta pozitiva in caz ca e negativa
        jge pozitiv
        
        mov ebx, 0
        sub ebx, eax 
        mov eax, ebx
        
    pozitiv:
        
        mov [diferenta], eax 
    
    ; printf(format_afisare, suma_pare, suma_impare, diferenta)
        push dword [diferenta]
        push dword [suma_impare]
        push dword [suma_pare]
        push dword format_afisare
        call [printf]
        add esp, 4*3
    
    ; printf(format, diferenta)
        ;push dword [diferenta]
        ;push dword format 
        ;call [printf]
        ;add esp, 4*2
        
    ; printf(format_hexa, suma_pare, suma_impare, diferenta)
        push dword [diferenta]
        push dword [suma_impare]
        push dword [suma_pare]
        push dword format_hexa 
        call [printf]
        add esp, 4*4
        
    ; fopen(nume_fisier, mod_acces)
        push dword mod_acces
        push dword nume_fisier
        call [fopen]
        add esp, 4*2
        
        cmp eax, 0
        je final 
        
        mov [descriptor_fisier], eax 
        
    ; fprintf(descriptor_fisier, format_hexa, suma_pare, suma_impare, diferenta)
        push dword [diferenta]
        push dword [suma_impare]
        push dword [suma_pare]
        push dword format_hexa
        push dword [descriptor_fisier]
        call [fprintf]
        add esp, 4*4
    
    ; fprintf(descriptor_fisier, format_hexa_fisier, suma_pare)
        push dword [suma_pare]
        push dword format_hexa_fisier
        push dword [descriptor_fisier]
        call [fprintf]
        add esp, 4*3
        
        push dword [suma_impare]
        push dword format_hexa_fisier
        push dword [descriptor_fisier]
        call [fprintf]
        add esp, 4*3
        
        push dword [diferenta]
        push dword format_hexa_fisier
        push dword [descriptor_fisier]
        call [fprintf]
        add esp, 4*3
        
    ; fclose(descriptor_fisier)
        push dword [descriptor_fisier]
        call [fclose]
        add esp, 4*1
        
        
    final:

push dword 0
call [exit]




