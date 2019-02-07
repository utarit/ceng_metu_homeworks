#include "Blockchain.h"
#include "Utilizer.h"
#include <iomanip>
/*
YOU MUST WRITE THE IMPLEMENTATIONS OF THE REQUESTED FUNCTIONS
IN THIS FILE. START YOUR IMPLEMENTATIONS BELOW THIS LINE 
*/

Blockchain::Blockchain(int id): id(id), head(NULL) 
{
    softBlockchain = false;
}

Blockchain::Blockchain(int id, Koin *headKoin): id(id) 
{
    head = headKoin;
    softBlockchain = false;
}

Blockchain::Blockchain(const Blockchain& rhs): id(rhs.id) 
{
    softBlockchain = rhs.softBlockchain;
    Koin *p = rhs.getHead();
    head = new Koin(p->getValue());
    p = p->getNext();
    Koin *n = head;
    Koin *newKoin;

    while(p != NULL)
    {
        newKoin = new Koin(p->getValue());
        n->setNext(newKoin);
        n = n->getNext();
        p = p->getNext();
    }
}

void Blockchain::cleanChain()
{
    if(head == NULL) return;
    if(softBlockchain) return;
    Koin *c = head;
    Koin *n = head->getNext();

    while(n != NULL)
    {
        c = n;
        n = n->getNext();
        delete c;
    }

    delete head;

    head = NULL;
}

Blockchain& Blockchain::operator=(Blockchain&& rhs) noexcept 
{
    cleanChain();

    Koin *p = rhs.getHead();
    head = new Koin(p->getValue());
    p = p->getNext();
    Koin *n = head;
    Koin *newKoin;

    while(p != NULL)
    {
        newKoin = new Koin(p->getValue());
        n->setNext(newKoin);
        n = n->getNext();
        p = p->getNext();
    }

    rhs.cleanChain();
    
}

Blockchain::~Blockchain() 
{
    cleanChain();
}

int Blockchain::getID() const 
{
    return id;
}

Koin* Blockchain::getHead() const 
{
    return head;
}

double Blockchain::getTotalValue() const 
{
    double sum = 0;
    Koin *p = head;

    while(p != NULL)
    {
        sum += p->getValue();
        p = p->getNext();
    }

    return sum;
}

long Blockchain::getChainLength() const 
{

    long length = 0;
    Koin *p = head;

    while(p != NULL)
    {
        length++;
        p = p->getNext();
    }

    return length;
}

void Blockchain::operator++() 
{
    Koin *minedKoin = new Koin(Utilizer::fetchRandomValue());

    Koin *p = head;
    if(p == NULL)
    {
        head = minedKoin;
        return;
    }

    while(p->getNext() != NULL)
    {
        p = p->getNext();
    }

    p->setNext(minedKoin);
}

void Blockchain::operator--() 
{
     if(head == nullptr){
        return;
     } 

     Koin *p = head;
     Koin *n = head->getNext();

     if(n == nullptr)
     {
        if(!head->softForked())
        {
            delete head;
            head = nullptr;
        }
        return;
     }

    while(n->getNext() != nullptr)
    {
        p = n;
        n = n->getNext();
    }

    if(n->softForked()) return;
    p->setNext(nullptr);
    delete n;

}

Blockchain& Blockchain::operator*=(int multiplier)
{ 
    Koin *p = head;
    double res;

    if(head == NULL) return (*this);

    while(p != NULL)
    {
        res  = multiplier * p->getValue();
        p->setValue(res);
        p = p->getNext();
    }

    return *this; 

}

Blockchain& Blockchain::operator/=(int divisor){ 
    Koin *p = head;
    double res;

    if(head == NULL) return *this;

    while(p != NULL)
    {
        res  = p->getValue() / divisor ;
        p->setValue(res);
        p = p->getNext();
    }

    return *this; 
}

std::ostream& operator<<(std::ostream& os, const Blockchain& blockchain) {
    if(blockchain.getHead() == NULL)
    {
        os << "Block " << blockchain.id << ": Empty.";
        return os;
    }

    os << "Block " << blockchain.id << ": ";
    
    Koin *p;

    for(p = blockchain.getHead(); p != NULL; p = p->getNext())
    {
        os << std::fixed << std::setprecision(Utilizer::koinPrintPrecision()) << p->getValue() << "--";
        if(p->getNext() == NULL)
            os << "|";
    }

    os << "(" << blockchain.getTotalValue() << ")" ;

    return os;
}


