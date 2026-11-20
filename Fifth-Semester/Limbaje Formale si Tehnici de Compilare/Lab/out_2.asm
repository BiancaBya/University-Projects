bits 32

global start
extern exit, printf, scanf

import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll

segment data use32 class = data

    format_in  db "%d", 0
    format_out db "%d", 13, 10, 0

    z dd 0
    y dd 0
    x dd 0

segment code use32 class = code
    start:
    push dword x
    push dword format_in
    call [scanf]
    add esp, 4*2
    push dword y
    push dword format_in
    call [scanf]
    add esp, 4*2
    mov eax, [x]
    push eax
    mov eax, [y]
    pop ecx
    add eax, ecx
    push eax
    mov eax, [x]
    push eax
    mov eax, 2
    pop ecx
    sub ecx, eax
    mov eax, ecx
    pop ecx
    imul eax, ecx
    mov [z], eax
    push dword [z]
    push dword format_out
    call [printf]
    add esp, 4*2

    push dword 0
    call [exit]
