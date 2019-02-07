#include "Berserk.h"

/*
YOU MUST WRITE THE IMPLEMENTATIONS OF THE REQUESTED FUNCTIONS
IN THIS FILE. START YOUR IMPLEMENTATIONS BELOW THIS LINE 
*/

Berserk::Berserk(uint id, int x, int y) : Player(id, x, y)
{
    HP = 100;
}

Weapon Berserk::getWeapon() const
{
    return PISTOL;
}


Armor Berserk::getArmor() const
{
    return WOODEN;
}

std::vector<Move> Berserk::getPriorityList() const
{
    return { ATTACK, UP, LEFT, DOWN, RIGHT };
}

const std::string Berserk::getFullName() const
{
    std::stringstream ss;
    ss << "Berserk" << getBoardID();
    std::string s = ss.str();

    return s;
}

