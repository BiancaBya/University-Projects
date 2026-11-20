;Read a sentence from the keyboard. Count the letters from each word and print the numbers on the screen.

bits 32

global start

extern exit, scanf, printf
import exit msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll


segment data use32 class=data
    text times 101 db 0
    format1 db "%s", 0
    cnt times 202 db 0
    

; procedure definition
lettercounter:     
    mov esi, [esp+4]
    mov eax, 0
    .count:
        mov bl, [esi+eax]
        cmp bl, 0
        je .end_count
        inc eax
    loop .count
    .end_count:
	ret 4

    
    
    
segment code use32 class=code
    start:
        mov ebx, 0
        start_loop:
            ;read word from keyboard
            push dword text
            push dword format1
            call [scanf]
            add esp, 4*2
            
            ;if we read a ".", stop reading
            cmp byte [text], "."
            je end_loop
            
            mov esi, 0
            mov ecx, 30
            push dword ebx
            
            push dword text
            call lettercounter
            
            pop ebx
            
            mov [cnt+ebx], eax
            add [cnt+ebx], byte 48
            inc ebx
            mov [cnt+ebx], byte " "
            inc ebx
            
        loop start_loop
            
        end_loop:
        
        mov [cnt+ebx], byte 0
        
        push dword cnt
        push dword format1
        call [printf]
        add esp, 4*2
        
push dword 0
call [exit]


