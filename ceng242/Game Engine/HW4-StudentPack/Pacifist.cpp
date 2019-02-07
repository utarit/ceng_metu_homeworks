#include "Pacifist.h"

/*
YOU MUST WRITE THE IMPLEMENTATIONS OF THE REQUESTED FUNCTIONS
IN THIS FILE. START YOUR IMPLEMENTATIONS BELOW THIS LINE 
*/

Pacifist::Pacifist(uint id, int x, int y) : Player(id, x, y)
{
    HP = 100;
}

Weapon Pacifist::getWeapon() const
{
    return NOWEAPON;
}

Armor Pacifist::getArmor() const
{
    return METAL;
}

std::vector<Move> Pacifist::getPriorityList() const
{
    return { UP, LEFT, DOWN, RIGHT };
}


const std::string Pacifist::getFullName() const
{
    std::stringstream ss;
    ss << "Pacifist" << getBoardID();
    std::string s = ss.str();

    return s;
}