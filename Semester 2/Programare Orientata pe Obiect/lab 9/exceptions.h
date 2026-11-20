#ifndef LAB9_EXCEPTIONS_H
#define LAB9_EXCEPTIONS_H
#include <string>

class Exceptions{

protected:

    std::string message;

public:

    [[nodiscard]] std::string get_message() const { return message; }

};


class IndexInvalid : public Exceptions{

public:

    IndexInvalid() noexcept { message = "Pozitie invalida!";}

};


class CarteExistenta : public Exceptions{

public:

    CarteExistenta() noexcept {message = "Cartea exista deja!";}

};


class Dateinvalide : public Exceptions{

public:

    Dateinvalide() noexcept {message = "Date invalide!";}

};


class CarteaNuExista : public Exceptions{

public:

    CarteaNuExista() noexcept {message = "Cartea nu exista!";}

};

#endif //LAB9_EXCEPTIONS_H
