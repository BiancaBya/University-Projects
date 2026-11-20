;Sa se scrie un program care citeste un fisier text care contine propozitii (propozitiile sunt separate de caracterul '/') si scrie intr-un alt fisier ;doar propozitiile de ordin de forma 2k+1 (i.e. scrie propozitia 1, 3, 5 ..). Numele celor 2 fisiere se dau in segmentul de date.

bits 32 

global start        

extern exit, fopen, fclose, scanf, printf, fscanf, fread, fprintf, fwrite          
import exit msvcrt.dll    
import fclose msvcrt.dll
import fopen msvcrt.dll
import fscanf msvcrt.dll
import printf msvcrt.dll 
import scanf msvcrt.dll     
import fread msvcrt.dll
import fprintf msvcrt.dll
import fwrite msvcrt.dll

segment data use32 class=data
   
   nume_input db "input.txt", 0
   nume_output db "output.txt", 0
   
   descriptor_input dd -1
   descriptor_output dd -1
   
   mod_input db "r", 0
   mod_output db "a", 0
   
   format_s db "%s ", 0
   contor dd 1
   
   len equ 100
   propozitie times len db 0
   text times len db 0
   

segment code use32 class=code
    start:
        ;fopen (nume_input, mod_input)
            push dword mod_input
            push dword nume_input
            call [fopen]
            add esp, 4*2
            
            cmp eax, 0
            je caput
            
            mov [descriptor_input], eax 
            
        ; fopen(nume_output, mod_output)
            push dword mod_output
            push dword nume_output
            call [fopen]
            add esp, 4*2
            
            cmp eax, 0
            je caput 
            mov [descriptor_output], eax 
        
        ;citim tot textul din fisier  
        ; fread (text, len, 1, descriptor_input)
            push dword [descriptor_input]
            push dword 1
            push dword len 
            push dword text 
            call [fread]
            add esp, 4*4
            
        ; fclose(descriptor_input)
            push dword [descriptor_input]
            call [fclose]
            add esp, 4*1
            
            
        mov esi, text 
        
        parcurgere_text:
            
            mov edi, propozitie
            mov ecx, len 
            zero:
                mov al, 0
                stosb 
            loop zero
            
            mov edi, propozitie 
            
            pus_in_propozitie:
            
                lodsb 
                cmp al, 0
                je sfarsit_parcurgere
                
                cmp al, "/"
                je gata_propozitia
                stosb
                
            jmp pus_in_propozitie
            
        gata_propozitia:
            test dword [contor], 01h
            jz nu_afisam
            
            ;fprintf(descriptor_output, format_s, propozitie)
                push dword propozitie
                push dword format_s
                push dword [descriptor_output]
                call [fprintf]
                add esp, 4*3
            
        nu_afisam:
            
            inc dword [contor]
            
        jmp parcurgere_text
        
        sfarsit_parcurgere:
        
        
        test dword [contor], 01h
        jz nu
        ;fprintf(descriptor_output, format_s, propozitie)
                push dword propozitie
                push dword format_s
                push dword [descriptor_output]
                call [fprintf]
                add esp, 4*3
        
        nu:
        ;fclose(descriptor_output)
            push dword [descriptor_output]
            call [fclose]
            add esp, 4*1
        
    caput:
    
push dword 0      
call [exit]       

