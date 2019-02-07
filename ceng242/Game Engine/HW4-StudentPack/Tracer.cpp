#include "Tracer.h"

/*
YOU MUST WRITE THE IMPLEMENTATIONS OF THE REQUESTED FUNCTIONS
IN THIS FILE. START YOUR IMPLEMENTATIONS BELOW THIS LINE 
*/

Tracer::Tracer(uint id, int x, int y) : Player(id, x, y)
{
    HP = 100;
}

Weapon Tracer::getWeapon() const
{
    return SHOVEL;
}

Armor Tracer::getArmor() const
{
    return BRICK;
}

std::vector<Move> Tracer::getPriorityList() const
{
    return { UP, LEFT, DOWN, RIGHT, ATTACK };;
}

const std::string Tracer::getFullName() const
{
    std::stringstream ss;
    ss << "Tracer" << getBoardID();
    std::string s = ss.str();

    return s;
}