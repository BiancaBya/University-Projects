bits 32 

global start        

extern exit, scanf, printf
          
import exit msvcrt.dll    
import scanf msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    
    format db "%s", 0
    text times 101 db 0
    sir_lungimi times 202 db 0 ; de 202 ori pentru a pune si spatii intre cifre
    mesaj db "Textul este:", 13, 10, 0

segment code use32 class=code
    start:
        
    ; printf(mesaj)
        push dword mesaj 
        call [printf]
        add esp, 4*1
        
        mov ebx, 0 ; contorul care merge pe sirul sir_lungimi
    citire:  
    
        ; scanf(format, text)
            push dword text
            push dword format
            call [scanf]
            add esp, 4*2
    
        
            cmp byte [text], "."
            je sfarsit
            
            mov esi, text 
            mov edx, 0 ; in ebx numaram nr de litere ale cuvantului
            
        numarare:
        
            lodsb
            cmp al, 0
            je peste
            inc edx 
        
        loop numarare
        
        peste:
        
            mov [sir_lungimi + ebx], edx 
            add byte [sir_lungimi + ebx], 48 ; codul ascii al lui 0 ca sa dea codul ascii al numarului de cifre curent
            inc ebx 
            mov byte [sir_lungimi + ebx], " "
            inc ebx 
        
    jmp citire 

    
    sfarsit:
        
        mov byte [sir_lungimi + ebx], 0
    
    ; printf(format, sir_lungimi)
        push dword sir_lungimi
        push dword format
        call [printf]
        add esp, 4*2
    

push dword 0      
call [exit]  

