//4. Determina primele n cifre din scrierea fractiei subunitare
//k/m = 0.c1c2c3..., pentru k si m numere naturale date.




#include <stdio.h>



int afisare_cifre(int n, float numar){

    /*
        Functia primeste numarul n si afiseaza primele n cifre zecimale ale variabilei numar
        n -> de tip int, numarul de cifre e afisat
        numar -> de tip float, numarul zecimal
        functia nu returneaza nimic, doar afiseaza cifrele
    */

    int cifra;

    while (n > 0){
        numar *= 10;
        cifra = (int)numar % 10;
        printf("%d\n", cifra);
        n--;
    }

}


int main(){

    int n;
    float k, m;
    while (1) {

        printf( "Introduceti 0 pentru a termina\nn=");
        scanf("%d", &n);

        if (n == 0)
            break;

        printf("%s", "k=");
        scanf("%d", &k);
        printf("%s", "m=");
        scanf("%d", &m);

        float numar = k / m;
        afisare_cifre(n, numar);

    }

    return 0;

}







