#include "Dummy.h"

/*
YOU MUST WRITE THE IMPLEMENTATIONS OF THE REQUESTED FUNCTIONS
IN THIS FILE. START YOUR IMPLEMENTATIONS BELOW THIS LINE 
*/

Dummy::Dummy(uint id, int x, int y) : Player(id, x, y)
{
    HP = 1000;
}

Weapon Dummy::getWeapon() const
{
    return NOWEAPON;
}

Armor Dummy::getArmor() const
{
    return NOARMOR;
}

std::vector<Move> Dummy::getPriorityList() const
{
    return { NOOP };
}

const std::string Dummy::getFullName() const
{
    std::stringstream ss;
    ss << "Dummy" << getBoardID();
    std::string s = ss.str();

    return s;
}