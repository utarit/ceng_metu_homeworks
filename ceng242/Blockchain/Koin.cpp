#include "Koin.h"
#include "Utilizer.h"

#include <iostream>
#include <iomanip>
/*
YOU MUST WRITE THE IMPLEMENTATIONS OF THE REQUESTED FUNCTIONS
IN THIS FILE. START YOUR IMPLEMENTATIONS BELOW THIS LINE 
*/

Koin::Koin(double initialValue) {
    value = initialValue;
    this->next = NULL;
    softKoin = false;
}

Koin::Koin(const Koin& rhs) {
    value = rhs.value;
    this->next = rhs.next;
    softKoin = rhs.softKoin;
}

Koin::~Koin() {
}

Koin& Koin::operator=(const Koin& rhs) {
    softKoin = rhs.softKoin;
    value = rhs.value;
    this->next = rhs.next;
    return *this;
}

double Koin::getValue() const {
    return value;
}

Koin* Koin::getNext() const {
    return next;
}

void Koin::setNext(Koin *nextValue) {
    next = nextValue;
}

void Koin::setValue(double newVal) {
    value = newVal;
}

bool Koin::disEqualityCheck(const Koin* first, const Koin* second) const {
    if(first == NULL && second == NULL) return false;
    if((first == NULL && second != NULL) || (first != NULL && second == NULL)) return true;

    if(first->value != second->value) return true;
    else return false || disEqualityCheck(first->next, second->next);
}

bool Koin::equalityCheck(const Koin* first, const Koin* second) const {
    if(first == NULL && second == NULL) return true;
    if((first == NULL && second != NULL) || (first != NULL && second == NULL)) return false;

    if(first->value == second->value) return true && equalityCheck(first->next, second->next);
    else return false;
}

bool Koin::operator==(const Koin& rhs) const {
    if(value - rhs.value <= Utilizer::doubleSensitivity() || rhs.value - value <= Utilizer::doubleSensitivity()) 
        return true && equalityCheck(this->next, rhs.next);
    else 
        return false;
}

bool Koin::operator!=(const Koin& rhs) const {
    if(value - rhs.value >= Utilizer::doubleSensitivity() || rhs.value - value >= Utilizer::doubleSensitivity())
        return true;
    else 
        return false || disEqualityCheck(this->next, rhs.next);
}

Koin& Koin::operator*=(int multiplier) {
    value *= multiplier;
    return (*this);
}

Koin& Koin::operator/=(int divisor) {
    value /= divisor;
    return (*this);
}

std::ostream& operator<<(std::ostream& os, const Koin& koin) {
    Koin *p;

    os << std::fixed << std::setprecision(Utilizer::koinPrintPrecision()) << koin.value << "--";

    for(p = koin.next; p != NULL; p = p->next)
    {
        os << std::fixed << std::setprecision(Utilizer::koinPrintPrecision()) << p->value << "--";
        if(p->next == NULL)
            os << "|";
    }

    return os;
}

void Koin::setSoftKoin(bool val)
{
    softKoin = val;
}

bool Koin::softForked(){
    return softKoin;
}

