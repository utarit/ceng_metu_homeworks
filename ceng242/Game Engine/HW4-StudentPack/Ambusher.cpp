#include "Ambusher.h"

/*
YOU MUST WRITE THE IMPLEMENTATIONS OF THE REQUESTED FUNCTIONS
IN THIS FILE. START YOUR IMPLEMENTATIONS BELOW THIS LINE 
*/
Ambusher::Ambusher(uint id, int x, int y) : Player(id, x, y)
{
    HP = 100;
}

Weapon Ambusher::getWeapon() const
{
    return SEMIAUTO;
}

Armor Ambusher::getArmor() const
{
    return NOARMOR;
}

std::vector<Move> Ambusher::getPriorityList() const
{
    return { ATTACK };
}

const std::string Ambusher::getFullName() const
{
    std::stringstream ss;
    ss << "Ambusher" << getBoardID();
    std::string s = ss.str();

    return s;
}
