#include "GameEngine.h"

/*
YOU MUST WRITE THE IMPLEMENTATIONS OF THE REQUESTED FUNCTIONS
IN THIS FILE. START YOUR IMPLEMENTATIONS BELOW THIS LINE 
*/
GameEngine::GameEngine(uint boardSize, std::vector<Player *> *players) : board(boardSize, players)
{
    currentRound = 0;
}

GameEngine::~GameEngine(){
    std::vector<Player *> *players = board.getPlayers();

    for(Player *player : *players) {
        delete player;
    }

    players->clear();
    delete players;
}

const Board& GameEngine::getBoard() const {
    return board;
}

Player* GameEngine::operator[](uint id) const {
     std::vector<Player *> *players = board.getPlayers();

    for(Player *player : *players) {
        if(player->getID() == id) return player;
    }

    return nullptr;

}

bool GameEngine::isFinished() const {
    std::vector<Player *> *players = board.getPlayers();
    if((*players).empty()) return true;
    if((*players).size() == 1 && board.isCoordHill((*players)[0]->getCoord())) return true;
    return false;

    /*
    std::vector<Player *> *players = board.getPlayers();

    Player *p;

    int numberOfAlive = 0;

    for(Player *player : *players) {
        if(!(player->isDead())) numberOfAlive++;
    }

    if(numberOfAlive == 0 ) return true;

    if(numberOfAlive == 1)
    {
        for(Player *player : *players) {
            if(!(player->isDead())) {
                p = player;
                break;
            }
        }

        if (board.isCoordHill(p->getCoord()))
            return true;
    }

    return false;
    */



}

void GameEngine::takeTurn(){
    currentRound++;
    std::vector<Player *> *players = board.getPlayers();
    int prevPlayer = -1;
    int id;
    int currentPlayer = INT16_MAX;

    std::cout << "-- START ROUND "<< currentRound << " --\n";
    board.updateStorm(currentRound);

    
    while (true)
    {
        for(Player *player : *players)
        {
            id = player->getID();
            if(player != NULL && id > prevPlayer && id < currentPlayer )
            {
                currentPlayer = player->getID();
            }

        }
        if(currentPlayer == INT16_MAX) break;
        takeTurnForPlayer(currentPlayer);
        prevPlayer = currentPlayer;
        currentPlayer = INT16_MAX;
    }


    std::cout << "-- END ROUND "<< currentRound << " --\n";
    
}

Move GameEngine::takeTurnForPlayer(uint playerID){
    
    std::vector<Player *> *players = board.getPlayers();
    Player *p = NULL;
    int index;
    for(int i = 0; i < (*players).size(); i++) {
        if((*players)[i]->getID() == playerID) {
            p = (*players)[i];
            index = i;
        } 
    }

    if(p == NULL) return NOOP;


    std::vector<Move> pList = p->getPriorityList();
    std::vector<Coordinate> visibleCoord = board.visibleCoordsFromCoord(p->getCoord());

    if(board.isStormInCoord(p->getCoord()))
    {
        std::cout << p->getFullName() << "(" << p->getHP() << ") is STORMED! (-" << Entity::stormDamageForRound(currentRound) << ")\n";
        p->takeDamage(Entity::stormDamageForRound(currentRound));
        if(p->isDead()){
            std::cout << p->getFullName() << "(" << p->getHP() << ") DIED.\n";
            (*players).erase((*players).begin()+index); 
            delete p;
            p = NULL;

            return NOOP;
        }
    }

    for(Move move : pList)
    {
        if(move == NOOP) return NOOP;
        else if(move == ATTACK)
        {
            std::vector<Player *> attackable;
            for(Coordinate coord : visibleCoord)
                if(board[coord] != NULL) attackable.push_back(board[coord]);

            if(attackable.empty()) continue;
                else {
                    Player *v;
                    v = attackable[0];
                    if(attackable.size() > 1) 
                    {
                        for(Player *a : attackable)
                        {
                            if(a->getID() < v->getID()) v = a;
                        }
                    }

                    bool isPlayerDead = p->attackTo(v);
                    if(isPlayerDead)
                    {
                        std::cout << v->getFullName() << "(" << v->getHP() << ") DIED.\n";
                        //Burada Hata alabilirsin
                        for(int i = 0; i < (*players).size(); i++) {
                            if((*players)[i]->getID() == v->getID()) index = i;
                        }
                        (*players).erase((*players).begin()+index);
                        delete v;
                        v = NULL;

                    }
                    return ATTACK;
                }
        }
        else
        {
            Coordinate newCoord = board.calculateCoordWithMove(move, p->getCoord());
            Coordinate hill = Coordinate(board.getSize() / 2, board.getSize()/2);
            int dist = hill - p->getCoord();
            int newdist = hill - newCoord;


            if(newCoord != p->getCoord() && newdist < dist)
            {
                p->executeMove(move);
                return move;
            } else {
                continue;
            }
        }
    }
}

Player* GameEngine::getWinnerPlayer() const {
    std::vector<Player *> *players = board.getPlayers();
    if(isFinished() && (*players).size() == 1) return (*players)[0];
}