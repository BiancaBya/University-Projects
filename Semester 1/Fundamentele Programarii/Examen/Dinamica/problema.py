# sublista crescatoare cea mai lunga cu numere din 2 cifre
# recurenta si implementarea iterativa

def longest_increasing_sublist(lst):
    n = len(lst)
    if n == 0:
        return []

    # Initializăm o listă pentru a stoca lungimile celor mai lungi subliste crescătoare
    s = [1] * n

    # Initializăm o listă pentru a stoca indexul anterior al celei mai lungi subliste crescătoare
    p = [-1] * n

    # Parcurgem lista și actualizăm lungimile sublistelor crescătoare
    for i in range(1, n):
        for j in range(i):
            if lst[i] > lst[j] and s[i] < s[j] + 1:
                s[i] = s[j] + 1
                p[i] = j

    # Găsim lungimea maximă din lista de lungimi
    max_length = max(s)

    # Găsim indexul la care se află lungimea maximă
    max_length_index = s.index(max_length)

    # Construim cea mai lungă sublistă crescătoare
    result = []
    while max_length_index != -1:
        result.insert(0, lst[max_length_index])
        max_length_index = p[max_length_index]

    return result


# Citim lista de la tastatură
input_list = [int(x) for x in input("Introduceti lista de numere separate prin spatiu: ").split()]

# Filtram lista pentru a pastra doar numerele cu doua cifre
filtered_list = [x for x in input_list if 10 <= x <= 99]

# Găsim și afișăm cea mai lungă sublistă crescătoare cu numere din 2 cifre
result = longest_increasing_sublist(filtered_list)
print("Cea mai lunga sublista crescatoare cu numere din 2 cifre este:", result)

