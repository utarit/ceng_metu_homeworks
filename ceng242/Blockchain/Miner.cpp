#include "Miner.h"
#include "Utilizer.h"
#include <iomanip>

/*
YOU MUST WRITE THE IMPLEMENTATIONS OF THE REQUESTED FUNCTIONS
IN THIS FILE. START YOUR IMPLEMENTATIONS BELOW THIS LINE 
*/

int Miner::getNextAvailableBlockchainID() const {
    return list.size();
}

Miner::Miner(std::string newName){
    name = newName;
}

Miner::~Miner() {
    

    for(int i = 0; i < list.size(); i++)
    {
        delete list[i];
        list[i] = NULL;
    }
}

void Miner::createNewBlockchain() {
    int newId = getNextAvailableBlockchainID();
    Blockchain *p = new Blockchain(newId);
    list.push_back(p);
}

void Miner::mineUntil(int cycleCount) {

    for(int i = 0; i < cycleCount; i++)
    {
        for(int j = 0; j < list.size(); j++)
        {
            ++(*list[j]);
        }
    }
}

void Miner::demineUntil(int cycleCount) {

    for(int i = 0; i < cycleCount; i++)
    {
        for(int j = 0; j < list.size(); j++)
        {
            --(*list[j]);
        }
    }
}

double Miner::getTotalValue() const {

    double sum = 0;
    
    for(int i = 0; i < list.size(); i++)
    {
        if(!(list[i]->softBlockchain))
            sum += list[i]->getTotalValue();
    }

    return sum;
}

long Miner::getBlockchainCount() const {
    return list.size();
}

Blockchain* Miner::operator[](int id) const {
    return list[id];
}

bool Miner::softFork(int blockchainID) {
    if(blockchainID >= list.size()) return false;

    Blockchain *b = list[blockchainID];
    Koin *h = b->getHead();
    if(h == nullptr) {
        createNewBlockchain();
        return true;
    }

    while(h->getNext() != nullptr)
    {
        h = h->getNext();
    }

    int newId = getNextAvailableBlockchainID();
    Blockchain *p = new Blockchain(newId, h);
    p->softBlockchain = true;
    h->setSoftKoin(true);
    list.push_back(p);

    return true;

}

bool Miner::hardFork(int blockchainID){
    if(blockchainID >= list.size()) return false;
    if(list[blockchainID] == NULL) return false;

    Blockchain *b = list[blockchainID];
    Koin *h = b->getHead();
    if(h == nullptr) return false;

    while(h->getNext() != nullptr)
    {
        h = h->getNext();
    }

    Koin *k = new Koin(h->getValue());

    int newId = getNextAvailableBlockchainID();
    Blockchain *p = new Blockchain(newId, k);
    list.push_back(p);

    return true;
}

std::ostream& operator<<(std::ostream& os, const Miner& miner) {
    os << "-- BEGIN MINER --\n";
    os << "Miner name: " << miner.name << "\n";
    os << "Blockchain count: " << miner.getBlockchainCount() << "\n";
    os << "Total value: " << std::fixed << std::setprecision(Utilizer::koinPrintPrecision()) << miner.getTotalValue() << "\n";
    os << "\n";

    for(int i = 0; i < (miner.list).size(); i++)
    {
        os << *(miner.list[i]) << "\n";
    }

    os << "\n";
    os << "-- END MINER --" << "\n";

    return os;
}
