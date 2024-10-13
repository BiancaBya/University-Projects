;Se citeste un nume de fisier . Sa se creeze un fisier cu numele dat, apoi sa se citeasca de la tastatura cuvinte pana cand se citeste de la tastatura caracterul '$'. Sa se scrie in fisier doar cuvintele care contin cel putin o litera mare (uppercase).
; next 21
bits 32 

global start        

extern exit , printf, scanf, fread, fopen, fclose, fprintf          
import exit msvcrt.dll    
import printf msvcrt.dll
import scanf msvcrt.dll
import fread msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fprintf msvcrt.dll

segment data use32 class=data
    

    mesaj_nume_fisier db "Numele fisierului este: ", 0
    mesaj_citire_sir db "Sirul citit este:", 13, 10, 0
    format_citire db "%s", 0
    
    nume_fisier times 50 db 0
    mod_scriere db "a", 0
    descriptor_fisier dd -1
    len equ 10
    text times len db 0
    
    copie resd 1

segment code use32 class=code
    start:
        
    ; printf (mesaj)
        push dword mesaj_nume_fisier 
        call [printf]
        add esp, 4*1
        
    ; scanf(format_citire, nume_fisier)
        push dword nume_fisier
        push dword format_citire
        call [scanf]
        add esp, 4*2
        
    ; EAX = fopen(nume_fisier, mod_acces)         1.NUME     2. MOD ACCES
        push dword mod_scriere
        push dword nume_fisier
        call [fopen]
        add esp, 4*2
        
        mov [descriptor_fisier], eax
        cmp eax, 0
        je final
        
    ; printf(mesaj_citire_sir)
        push dword mesaj_citire_sir
        call [printf]
        add esp, 4*1
      
        mov ecx, 1000000  
    citire:
        
        mov edi, text 
        mov ecx, len 
        pus_0_in_text:
            
            mov al, 0
            stosb
        
        loop pus_0_in_text
        
        ; scanf(format_citire, text)       
        push dword text
        push dword format_citire 
        call [scanf]
        add esp, 4*2
        
        cmp byte [text], "$"           ; !!!! ca sa opresti citirea unui sir cu un anumit caracter 
        je sfarsit                     ; cmp byte [sir_caractere], "caracter"   !!!!!    cmp byte [sir], "&"    !!!!
        
        mov ecx, len 
        mov esi, text 
        
        parcurgere_text:
        
            lodsb            ;|
                             ;|=> cum compari literele unui cuvant, cu operatii pe siruri si compari cu literele puse in ghilimele
            cmp al, "A"      ;|
            jb peste 
            cmp al, "z"
            ja peste
            
            cmp al, "A"
            jb peste
            cmp al, "Z"
            ja peste
            
        ; fprintf(descriptor_fisier, text)            FPRINTF -> 1. Descriptor_fisier     2. ce scrii, definit in segmentul de date 
            push dword text ; offset   ; in variabila text se afla cuvantul si asa se printeaza cuvantul in fisier, cu offset deci fara paranteze []
            push dword [descriptor_fisier]  
            call [fprintf]
            add esp, 4*2
            jmp afara
            
            
        peste:
        
        loop parcurgere_text
        
        afara:
    
    loop citire
    
    sfarsit:
        
        
        
    
    ; fclose(descriptor_fisier)
        push dword [descriptor_fisier]
        call dword [fclose]
        add esp, 4*1
        
    final:    
    
push dword 0      
call [exit]       



