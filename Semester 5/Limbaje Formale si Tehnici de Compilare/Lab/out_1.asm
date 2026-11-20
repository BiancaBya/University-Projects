bits 32

global start
extern exit, printf, scanf

import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll

segment data use32 class = data

    format_in  db "%d", 0
    format_out db "%d", 13, 10, 0

    c dd 0
    b dd 0
    a dd 0

segment code use32 class = code
    start:
    push dword a
    push dword format_in
    call [scanf]
    add esp, 4*2
    push dword b
    push dword format_in
    call [scanf]
    add esp, 4*2
    mov eax, [a]
    push eax
    mov eax, [b]
    push eax
    mov eax, 3
    pop ecx
    imul eax, ecx
    pop ecx
    add eax, ecx
    mov [c], eax
    push dword [c]
    push dword format_out
    call [printf]
    add esp, 4*2

    push dword 0
    call [exit]
