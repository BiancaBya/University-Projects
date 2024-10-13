#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <unordered_set>
#include <climits>

std::ifstream in;
std::ofstream out;

void PRIM(std::vector<std::vector<std::pair<int, int>>> &graph) {
    // Extrage numarul de noduri
    int nodes = static_cast<int>(graph.size());

    // Initializeaza distanta de la nodul sursa la celelalte noduri si a vectorului de parinti
    std::vector<int> dists(nodes, INT_MAX);
    std::vector<int> parent(nodes, -1);
    std::unordered_set<int> inMST;

    // incepe din nodul 0
    dists[0] = 0;
    std::priority_queue<std::pair<int, int>, std::vector<std::pair<int, int>>, std::greater<std::pair<int, int>>> pq;
    pq.push({0, 0});

    while (!pq.empty()) {
        // Extrage nodul curent
        int curr = pq.top().second;
        pq.pop();
        inMST.insert(curr); // Adauga nodul curent in MST

        // Compara distantele vecinilor nodului curent
        for (auto &neighs: graph[curr]) {
            int neigh = neighs.first;
            int dist = neighs.second;

            // Vede daca se pot face optimizari
            if (inMST.find(neigh) == inMST.end() && dist < dists[neigh]) {
                dists[neigh] = dist;
                parent[neigh] = curr;
                pq.push({dists[neigh], neigh});
            }
        }
    }

    // Calculeaza costul total al MST
    int totalCost = 0;
    for (int i = 0; i < nodes; ++i) {
        if (parent[i] != -1) {
            totalCost += dists[i];
        }
    }

    out << totalCost << std::endl;

    // pune in fisierul de output numarul de muchii
    int numEdges = nodes - 1;
    out << numEdges << std::endl;
    for (int i = 1; i < nodes; ++i) {
        out << parent[i] << " " << i << std::endl;
    }
}


int main(int argc, char *argv[]) {

    in.open("C:/Users/Next/CLionProjects/grafuritest/prim_in1.txt");
    out.open("C:/Users/Next/CLionProjects/grafuritest/prim_out1.txt");

    if (!in.is_open() || !out.is_open()) {
        std::cerr << "ERROR: unable to open files...\n";
        exit(1);
    }

    int node1, node2, weight;
    int nodes, edges;
    in >> nodes >> edges;

    std::vector<std::vector<std::pair<int, int>>> graph(nodes);

    // aici citeste graful din fisier si face perechi intre fiecare nod si ponderea muchiei dintre ele
    while (in >> node1 >> node2 >> weight) {
        graph[node1].emplace_back(std::make_pair(node2, weight));
        graph[node2].emplace_back(std::make_pair(node1, weight));
    }

    PRIM(graph);

    in.close();
    out.close();

    return 0;
}

//a)
// Algoritmul implementeaza algoritmul lui Prim care determina arborele partial de cost minim dintr-un
// graf neorientat si ponderat. Pe măsură ce nodurile sunt adăugate în MST, se actualizează distanțele pentru vecinii lor.
// Se utilizează priority_queue pentru a extrage nodul cu cea mai mică distanță care nu este încă în MST și se actualizează vectorii de distante si de parinti.
// Dupa rularea sa, in fisierul de iesire este scris pe prima linie costul total al grafului de cost minim,
// adica suma ponderilor muchiilor sale, dupa pe a doua linie este scris
// numarul de muchii din graful de cost minim si dupa sunt scrise muchiile acestuia pe restul liniilor


//b)
// un exemplu real in care algoritmul lui prim este folositor poate fi atunci cand un sofer merge cu masina in mai multe
// locatii si ar vrea sa consume cat mai putina benzina asa ca determina arborele de cost minim ca sa vada pe unde poate sa
// mearga in asa fel incat sa aiba consumatia minima



