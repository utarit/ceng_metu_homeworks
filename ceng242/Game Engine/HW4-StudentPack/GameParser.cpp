#include "GameParser.h"
#include "Ambusher.h"
#include "Dummy.h"
#include "Berserk.h"
#include "Tracer.h"
#include "Pacifist.h"

#include <fstream>
#include <iostream>

/*
YOU MUST WRITE THE IMPLEMENTATIONS OF THE REQUESTED FUNCTIONS
IN THIS FILE. START YOUR IMPLEMENTATIONS BELOW THIS LINE 
*/

std::pair<int, std::vector<Player *> *> GameParser::parseFileWithName(const std::string& filename)
{
    std::ifstream in(filename);

    std::string str;
    int i = 0;
    int boardSize = 0;
    std::vector<Player *> *list = new std::vector<Player *>;
    std::pair<int, std::vector<Player *> *> result;

    while (std::getline(in, str)) {
    // output the line

    if(i == 0)
    {
        boardSize = std::stoi(str.substr(12));
        i++;
    } else if (i == 1)
    {
        int playerNum = std::stoi(str.substr(14));
        (*list).reserve(playerNum);
        i++;

    } else{
        Player *p;

        int firstDot = str.find("::");
            int id = std::stoi(str.substr(0, firstDot));
        std::string substr2 = str.substr(firstDot + 2);
        int secondDot = substr2.find("::");
            std::string type = substr2.substr(0, secondDot);
        std::string substr3 = substr2.substr(secondDot + 2);
        int thirdDot = substr3.find("::");
            int x = std::stoi(substr3.substr(0, thirdDot));
            int y = std::stoi(substr3.substr(thirdDot + 2));

        if(type == "Ambusher")
        {
            p = new Ambusher(id, x, y);
        } else if(type == "Dummy")
        {
            p = new Dummy(id, x, y);
        } else if(type == "Berserk")
        {
            p = new Berserk(id, x, y);
        } else if(type == "Tracer")
        {
            p = new Tracer(id, x, y);
        } else if(type == "Pacifist")
        {
            p = new Pacifist(id, x, y);
        }

        (*list).push_back(p);

        i++;
    }

    // now we loop back and get the next line in 'str'
    }

    result.first = boardSize;
    result.second = list;

    return result;

}