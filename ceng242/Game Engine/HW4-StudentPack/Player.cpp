#include "Player.h"


/*
YOU MUST WRITE THE IMPLEMENTATIONS OF THE REQUESTED FUNCTIONS
IN THIS FILE. START YOUR IMPLEMENTATIONS BELOW THIS LINE 
*/



Player::Player(uint id, int x, int y) :id(id), coordinate(x,y) {}

Player::~Player(){}

uint Player::getID() const
{
    return id;
}

const Coordinate& Player::getCoord() const
{
    return coordinate;
}

int Player::getHP() const
{
    return HP;
}




std::string Player::getBoardID() const
{
    std::string bID = std::to_string(id);

    if(id >= 10)
    {
        return bID;

    } else {
        std::stringstream ss;
        ss << "0" << id;
        std::string s = ss.str();

        return s;
    }
}

bool Player::isDead() const 
{
    if(HP <= 0) return true;
    else return false;
}




void Player::executeMove(Move move)
{
    if(move == NOOP || move == ATTACK)
    {
        return;
    }
    coordinate = coordinate + move;

    //-playerFullName(playerHP)- moved UP/DOWN/LEFT/RIGHT.
    std::string s;
    switch(move)
    {
        case 1:
            s = "UP";
        break;
        case 2:
            s = "DOWN";
        break;
        case 3:
            s = "LEFT";
        break;
        case 4:
            s = "RIGHT";
        break;
        
    }

    std::cout << getFullName() << "(" << HP << ") moved " << s << ".\n"; 
}


bool Player::attackTo(Player *player)
{
    if(id == player->getID()) return false;

    //RHS's HP -= max((LHS's damage - RHS's armor), 0)
    int damage = std::max(Entity::damageForWeapon(getWeapon()) - Entity::damageReductionForArmor(player->getArmor()),0);

    //"-lhsFullName(lhsHP)- attacked -rhsFullName(rhsHP)-! (-damageDone-)

    std::cout << getFullName() << "(" << HP << ") ATTACKED " << player->getFullName() << "("<< player->getHP() << ")! (-" << damage <<")\n";
    player->takeDamage(damage);
    if(player->isDead()) return true;
    else return false;
}

void Player::takeDamage(int damage)
{
    HP -= damage;
}