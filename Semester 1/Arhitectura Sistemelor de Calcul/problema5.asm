;3.	Se citeste o propozitie de la tastatura. Sa se inverseze fiecare cuvant si sa se afiseze pe ecran.
bits 32 

global start        

extern exit, scanf, printf
          
import exit msvcrt.dll    
import scanf msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
   
    len equ 100
    text times len db 0
    text_mare times len db 0
    format db "%s", 0
    format_afisare db "Numarul de litere din cuvant este: %d", 13, 10, 0
    format_nr db "%d", 0
    numar_litere dd 0
   
segment code use32 class=code
    start:
    
        mov edi, text_mare
    citeste:
    
    ; scanf(format, text)
        push dword text 
        push dword format
        call [scanf]
        add esp, 4*2
        
        cmp byte [text], '.'
        je afara
        
        mov esi, text
        mov [numar_litere], dword 0
        parcurgere:
        
            lodsb
            cmp al, 0
            je iesi
            add [numar_litere], dword 1
            
        jmp parcurgere 
        
        iesi:
        
        ; printf(format_afisare, numar_litere)
            push dword [numar_litere]
            push dword format_afisare
            call [printf]
            add esp, 4*2
        
        mov al, [numar_litere]
        stosb
        
        ;mov al, ' '
        ;stosb
        
    jmp citeste
    
    afara:
        ;mov al, '.'
        ;stosb
    
    ; printf(format, text)
        ;push dword text_mare
        ;push dword format_nr
        ;call [printf]
        ;add esp, 4*2
        

push dword 0      
call [exit]       





