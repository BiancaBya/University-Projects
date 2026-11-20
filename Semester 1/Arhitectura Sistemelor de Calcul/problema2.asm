; se citeste n si m si apoi m cuvinte de la tastaturatoate pe linii diferite, sa se afiseze in fisier doar cuvintele
; care au numarul de vocale mai mare sau egal cu n 
bits 32 

global start        

extern exit, fopen, fclose, fprintf, scanf, printf             
import exit msvcrt.dll    
import fclose msvcrt.dll
import fopen msvcrt.dll
import fprintf msvcrt.dll
import printf msvcrt.dll 
import scanf msvcrt.dll 

segment data use32 class=data

    n dd 0
    m dd 0
    cuvant times 100 db 0
    sir_vocale db "aeiouAEIOU", 0
    mesaj_n db "n = ", 0 
    mesaj_m db "m = ", 0 
    mesaj_text db "Cuvintele sunt:", 13, 10, 0 
    format_s db "%s", 0
    format_d db "%d", 0
    format_afisare db "%s ", 0
    
    ; fisier 
    nume_fisier db "vocale.txt", 0
    mod_acces db "w", 0
    descriptor_fisier dd -1

segment code use32 class=code
    start:
    
    ; eax = fopen(nume_fisier, mod_acces)
        push dword mod_acces
        push dword nume_fisier
        call [fopen]
        add esp, 4*2
        
        cmp eax, 0
        je caput
        
        mov [descriptor_fisier], eax 
    
        
    ;printf(format, mesaj_n)
        push dword mesaj_n
        push dword format_s
        call [printf]
        add esp, 4*2
        
    ;scanf(format_d, n)
        push dword n 
        push dword format_d
        call [scanf]
        add esp, 4*2
        
    ;printf(format, mesaj_m)
        push dword mesaj_m
        push dword format_s
        call [printf]
        add esp, 4*2
        
    ;scanf(format_d, m)
        push dword m 
        push dword format_d
        call [scanf]
        add esp, 4*2

    ;printf(format, mesaj_text)
        push dword mesaj_text
        push dword format_s
        call [printf]
        add esp, 4*2
        
        
        mov ecx, [m]
        
    citire:
        
        push ecx
        
        ; scanf(format_s, cuvant)
            push dword cuvant 
            push dword format_s
            call [scanf]
            add esp, 4*2
            
        ; numaram numarul de vocale din cuvant in edx
            mov esi, cuvant 
            mov edx, 0
            parcurgere_cuvant:
            
                lodsb
                cmp al, 0  ; in al ave, litera curenta din cuvant
                je iesire

                mov edi, 0
                parcurgere_sir_vocale:
                
                    mov bl, [sir_vocale + edi] ; in bl avem litera curenta din sirul de vocale
                    inc edi
                    cmp bl, 0
                    je gata
                    
                    cmp al, bl 
                    jne sari
                    inc edx
                    jmp gata
                    
                sari:
                
                jmp parcurgere_sir_vocale
                
            gata:
                
            jmp parcurgere_cuvant
            
    iesire:
            
        cmp edx, [n]
        jb nu_bun
        
        ; fprintf(descriptor_fisier, format, cuvant)
            push dword cuvant
            push dword format_afisare
            push dword [descriptor_fisier]
            call [fprintf]
            add esp, 4*3
        
        nu_bun:
        
        pop ecx 
        
    loop citire
     

    ;fclose(descriptor_fisier)
        push dword [descriptor_fisier]
        call [fclose]
        add esp, 4*1
    
    caput:
        

push dword 0      
call [exit]       




