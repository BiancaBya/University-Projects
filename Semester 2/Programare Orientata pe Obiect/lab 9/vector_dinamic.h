#ifndef LAB9_VECTOR_DINAMIC_H
#define LAB9_VECTOR_DINAMIC_H
#define CAPACITATE_INITIALA 5

template<typename Element>
class IteratorVector;

template<typename Element>
class MyVector {
private:

    int dimensiune;
    int capacitate;
    Element* elemente;

    void asiguraCapacitate();

public:

    // constructor
    MyVector();

    // constructor de copiere
    MyVector(const MyVector& other);

    // destructor
    ~MyVector();

    // operatorul assignment
    MyVector& operator=(const MyVector& other);

    // move constructor
    MyVector(MyVector&& other);

    // operatrul =
    MyVector& operator=(MyVector&& other);


    void adauga(const Element& element);

    Element& get(int pozitieElement) const;

    void set(int pozitieElement, const Element& element);

    int size() const noexcept;

    void push_back(const Element& element);

    void pop_back();

    Element& operator[](int index) const;

    friend class IteratorVector<Element>;

    IteratorVector<Element> begin() const;

    IteratorVector<Element> end() const;

};


template<typename Element>
MyVector<Element>::MyVector() :elemente{new Element[CAPACITATE_INITIALA] }, capacitate{CAPACITATE_INITIALA },
                               dimensiune{ 0 } {}


template<typename Element>
MyVector<Element>::MyVector(const MyVector<Element>& other) {
    elemente = new Element[other.capacitate];

    for (int i = 0; i < other.dimensiune; i++) {
        elemente[i] = other.elemente[i];
    }
    dimensiune = other.dimensiune;
    capacitate = other.capacitate;
}


template<typename Element>
MyVector<Element>::~MyVector() {
    delete[] elemente;
}


template<typename Element>
MyVector<Element>& MyVector<Element>::operator=(const MyVector<Element>& other) {
    delete[] elemente;
    elemente = new Element[other.capacitate];

    for (int i = 0; i < other.dimensiune; i++) {
        elemente[i] = other.elemente[i];
    }
    dimensiune = other.dimensiune;
    capacitate = other.capacitate;
    return *this;
}


template<typename Element>
MyVector<Element>::MyVector(MyVector&& other) {
    elemente = other.elemente;
    dimensiune = other.dimensiune;
    capacitate = other.capacitate;

    other.elemente = nullptr;
    other.dimensiune = 0;
    other.capacitate = 0;
}


template<typename Element>
MyVector<Element>& MyVector<Element>::operator=(MyVector<Element>&& other) {
    if (this == &other) {
        return *this;
    }
    delete[] elemente;

    elemente = other.elemente;
    dimensiune = other.dimensiune;
    capacitate = other.capacitate;

    other.elemente = nullptr;
    other.dimensiune = 0;
    other.capacitate = 0;
    return *this;
}

template<typename Element>
void MyVector<Element>::adauga(const Element& element) {
    if (dimensiune == capacitate)
        asiguraCapacitate();
    elemente[dimensiune++] = element;
}

template<typename Element>
Element& MyVector<Element>::get(int pozitieElement) const {
    return elemente[pozitieElement];
}

template<typename Element>
void MyVector<Element>::set(int pozitieElement, const Element& element) {
    elemente[pozitieElement] = element;
}

template<typename Element>
int MyVector<Element>::size() const noexcept {
    return dimensiune;
}

template<typename Element>
void MyVector<Element>::asiguraCapacitate() {

    capacitate *= 2;
    Element* elementeAuxiliar = new Element[capacitate];
    for (int i = 0; i < dimensiune; i++) {
        elementeAuxiliar[i] = elemente[i];
    }
    delete[] elemente;
    elemente = elementeAuxiliar;
}

template<typename Element>
void MyVector<Element>::push_back(const Element& element) {
    if (dimensiune == capacitate) {
        asiguraCapacitate();
    }
    elemente[dimensiune] = element;
    dimensiune++;
}

template<typename Element>
void MyVector<Element>::pop_back() {
    dimensiune--;
}

template<typename Element>
Element& MyVector<Element>::operator[](int index) const{
    return elemente[index];
}

template<typename Element>
IteratorVector<Element> MyVector<Element>::begin() const {
    return IteratorVector<Element>(*this);
}

template<typename Element>
IteratorVector<Element> MyVector<Element>::end() const {
    return IteratorVector<Element>(*this, dimensiune);
}

template<typename Element>
class IteratorVector {
private:

    const MyVector<Element>& vector;
    int pozitieElement = 0;

public:

    IteratorVector(const MyVector<Element>& vector) noexcept;

    IteratorVector(const MyVector<Element>& vector, int pozitieElement) noexcept;

    bool valid() const noexcept;

    Element& element() const noexcept;

    void next() noexcept;

    Element& operator*();

    IteratorVector& operator++();

    bool operator==(const IteratorVector& other) noexcept;

    bool operator!=(const IteratorVector& other) noexcept;
};

template<typename Element>
IteratorVector<Element>::IteratorVector(const MyVector<Element>& vector) noexcept :vector{vector } {}

template<typename Element>
IteratorVector<Element>::IteratorVector(const MyVector<Element>& vector, int pozitieElement) noexcept :vector{
        vector }, pozitieElement{ pozitieElement } {}

template<typename Element>
bool IteratorVector<Element>::valid() const noexcept {
    return pozitieElement < vector.dimensiune;
}

template<typename Element>
Element& IteratorVector<Element>::element() const noexcept {
    return vector.elemente[pozitieElement];
}

template<typename Element>
void IteratorVector<Element>::next() noexcept {
    pozitieElement++;
}

template<typename Element>
Element& IteratorVector<Element>::operator*() {
    return element();
}

template<typename Element>
IteratorVector<Element>& IteratorVector<Element>::operator++() {
    next();
    return *this;
}

template<typename Element>
bool IteratorVector<Element>::operator==(const IteratorVector<Element>& other) noexcept {
    return pozitieElement == other.pozitieElement;
}

template<typename Element>
bool IteratorVector<Element>::operator!=(const IteratorVector<Element>& other) noexcept {
    return !(*this == other);
}


#endif //LAB9_VECTOR_DINAMIC_H
