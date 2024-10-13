; se da un numar n. sa se deschida un fisier in care se afla numere cu + sau - separate prin spatii
; sa se citeasca numerele din fisier si sa se afiseze pe ecran nr IMPARE cu EXACT N cifre
bits 32 

global start        

extern exit, fopen, fclose, fscanf, scanf, printf             
import exit msvcrt.dll    
import fclose msvcrt.dll
import fopen msvcrt.dll
import fscanf msvcrt.dll
import printf msvcrt.dll 
import scanf msvcrt.dll   

segment data use32 class=data
   
   ;fisier
   nume_fisier db "numere_scanf.txt", 0
   mod_acces db "r", 0
   descriptor_fisier dd -1
   
   n dd 0
   mesaj_n db "n = ", 0 
   numar dd 0
   format_d db "%d", 0
   format_afisare db "%d ", 0
   auxiliar dd 0
   
   numar_cifre dd 0
   semn dd 0
   
    
numarare_cifre:
    mov eax, [numar] ; sau mov ebx, [numar]  fara sa mai dau push dword [numar]
    mov dword [numar_cifre], 0 
    
    cmp eax, 0  ; vedem daca numarul este negativ
    jl negativ 
    mov dword [semn], 0  ; daca e pozitiv la semn punem 0
    jmp numarare
    
    negativ:
        
        mov dword [semn], 0FFFFFFFFh ; daca e negativ, la semn punem -1 = ffffffffh
        
        
    numarare:
        
        mov edx, [semn]
        mov ebx, 10
        idiv ebx ; imparti qword-ul edx:eax la ebx 
        inc dword [numar_cifre]
        cmp eax, 0 ; daca am terminat cifrele numarului
        jne numarare 

    ret 
   
   
segment code use32 class=code
    start:
    
    ; fopen(nume_fisier, mod_acces)
        push dword mod_acces
        push dword nume_fisier
        call [fopen]
        add esp, 4*2
        
        cmp eax, 0
        je caput
        mov [descriptor_fisier], eax 
        
    ; printf(mesaj_n)
        push dword mesaj_n
        call [printf]
        add esp, 4
        
    ; scanf(format_d, n)
        push dword n
        push dword format_d
        call [scanf]
        add esp, 4*2
         
        mov ecx, [n] 
        
    citire_numere:
        
        push ecx
        
        ; fscanf(descriptor_fisier, format_d, numar)
            push dword numar
            push dword format_d
            push dword [descriptor_fisier]
            call [fscanf]
            add esp, 4*3
            
        ; verificam daca numar este impar 
            test dword [numar], 01h  ; daca rezultatul e 0 inseamna ca e par 
            jz peste 
            
            cmp dx, 1 ; daca numarul este impar 
            jne peste
            
        ; verificam daca numar are exact n cifre 
            call numarare_cifre
            
            mov eax, [numar_cifre]
            cmp eax, [n]
            jne peste 
            
        ; printf(format_d, numar)
            push dword [numar]
            push dword format_afisare
            call [printf]
            add esp, 4*2    
            
            
        peste:    
        
        pop ecx 
        
    loop citire_numere
        
        
    ; fclose(descriptor_fisier)
        push dword [descriptor_fisier]
        call [fclose]
        add esp, 4
    

    caput:
    
push dword 0      
call [exit]       

            
            
; VERIFICAREA DACA ESTE PAR SAU IMPAR
; test dword [numar], 01h
; jz next      
; rezultatul este 1 atunci cand numarul e impar 




; impar:
    ; mov ebx, [esp + 4] ; ebx = numar  
    ; mov [auxiliar], ebx 
    ; mov ax, [auxiliar]
    ; mov dx, [auxiliar + 2]
    ; mov bx, 2
    ; idiv bx ; AX = catul, DX = restul
    
    ; ret 4


