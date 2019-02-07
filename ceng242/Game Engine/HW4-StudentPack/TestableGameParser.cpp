#include "TestableGameParser.h"

#include "GameParser.h"

void TestableGameParser::runInternal() {
    // Phase 1: Parse the given file.

    /**
     * Board Size: 3
     * Player Count: 2
     * 0::Tracer::2::0
     * 1::Berserk::2::2
     */

    /**
     * It should boil down to:
     *
     * Tracer(0, 2, 0)
     * Berserk(1, 2, 2)
     */

    std::cout << "Test 1: " << currentGrade << std::endl;
    auto parsedValues = GameParser::parseFileWithName(this->filenameToTest);
    std::cout << "Test 1.2: " << currentGrade << std::endl;

    if(parsedValues.first != 3)
        currentGrade -= 2;

    std::cout << "Test 2: " << currentGrade << std::endl;
    std::vector<Player *> *players = parsedValues.second;

    if(players->size() != 2)
        currentGrade -= 2;

    // Removing memory manually...
    // Normally GameEngine will/should do this.
    std::cout << "Test 3: " << currentGrade << std::endl;
    for(Player *player : *players) {
        delete player;
    }
    std::cout << "Test 4: " << currentGrade << std::endl;
    players->clear();
    std::cout << "Test 5: " << currentGrade << std::endl;
    delete players;
    std::cout << "Test 6: " << currentGrade << std::endl;
}
