#pragma once
#include "ab.h"
#include <stack>
#include <queue>

// Clasa de bază pentru iterator
class IteratorAB {
public:
    virtual void prim() = 0;
    virtual void urmator() = 0;
    virtual bool valid() const = 0;
    virtual TElem element() const = 0;
    virtual ~IteratorAB() = default;
};


class IteratorPreordine : public IteratorAB {
private:
    const AB& arbore;
    std::stack<const AB::Nod*> stiva;

public:
    IteratorPreordine(const AB& ab);
    void prim() override;
    void urmator() override;
    bool valid() const override;
    TElem element() const override;
};


class IteratorInordine : public IteratorAB {
private:
    const AB& arbore;
    std::stack<const AB::Nod*> stiva;

    void mergiLaStanga(const AB::Nod* p);

public:
    IteratorInordine(const AB& ab);
    void prim() override;
    void urmator() override;
    bool valid() const override;
    TElem element() const override;
};


class IteratorPostordine : public IteratorAB {
private:
    const AB& arbore;
    std::stack<const AB::Nod*> stiva1, stiva2;

    void init();

public:
    IteratorPostordine(const AB& ab);
    void prim() override;
    void urmator() override;
    bool valid() const override;
    TElem element() const override;
};


class IteratorLatime : public IteratorAB {
private:
    const AB& arbore;
    std::queue<const AB::Nod*> coada;

public:
    IteratorLatime(const AB& ab);
    void prim() override;
    void urmator() override;
    bool valid() const override;
    TElem element() const override;
};



