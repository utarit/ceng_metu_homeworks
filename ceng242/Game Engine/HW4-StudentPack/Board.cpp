#include "Board.h"
#include "Entity.h"

/*
YOU MUST WRITE THE IMPLEMENTATIONS OF THE REQUESTED FUNCTIONS
IN THIS FILE. START YOUR IMPLEMENTATIONS BELOW THIS LINE 
*/

    Board::Board(uint boardSize, std::vector<Player *> *players): boardSize(boardSize), players(players)
    {
        storm = 0;
    }
    Board::~Board(){}

    uint Board::getSize() const{
        return boardSize;
    }
    /**
     * Decide whether the coordinate is in the board limits.
     *
     * @param coord Coordinate to search.
     * @return true if coord is in limits, false otherwise.
     */
    bool Board::isCoordInBoard(const Coordinate& coord) const{
        return isCoordInSquare(coord, boardSize);
    }

    bool Board::isCoordInSquare(const Coordinate& coord, uint size) const {
        uint center = boardSize / 2;
        uint dist = size / 2;
        uint up, down;

        up = center + dist;
        down = center - dist;

        if(coord.x <= up && coord.x >= down && coord.y <= up && coord.y >= down)
            return true;
        else 
            return false;
    }
    /**
     * Decide whether the given coordinate is in storm.
     *
     * @param coord Coordinate to search.
     * @return true if covered in storm, false otherwise.
     */
    bool Board::isStormInCoord(const Coordinate &coord) const{
        return isCoordInSquare(coord, boardSize) && !isCoordInSquare(coord, boardSize - 2*storm);
    }

    /**
     * Decide whether the given coordinate is the hill.
     *
     * @param coord Coordinate to search.
     * @return true if the coord is at the very center of the board, false otherwise.
     */
    bool Board::isCoordHill(const Coordinate& coord) const{
        int center = boardSize / 2;
        if(coord == Coordinate(center, center))
            return true;
        else 
            return false;
    }
    /**
     * Indexing.
     *
     * Find the player in coordinate.
     *
     * nullptr if player does not exists in given coordinates, or !isCoordInBoard
     *
     * @param coord  The coordinate to search.
     * @return The player in coordinate.
     */
    Player* Board::operator[](const Coordinate& coord) const{
        if(!isCoordInBoard(coord)) return nullptr;

        for(int i = 0; i < (*players).size(); i++)
        {
            if((*players)[i]->getCoord() == coord) return (*players)[i];
        }

        return nullptr;
    }
    /**
     * Calculate the new coordinate with the given move and coordinate.
     *
     * NOOP and ATTACK are no-op, return coord.
     *
     * The new coordinate cannot be outside of the borders.
     * If it's the case, return coord.
     *
     * Also, if there's another player in the new coordinate,
     * return coord.
     *
     * @param move Move to be made.
     * @param coord The coordinate to be moved.
     * @return Calculated coordinate after the move.
     */
    Coordinate Board::calculateCoordWithMove(Move move, const Coordinate &coord) const{
        if(move == NOOP || move == ATTACK) return coord;
        if(!isCoordInBoard(coord + move)) return coord;
        if((*this)[coord + move] != nullptr) return coord;

        return coord + move;
    }

    /**
     * Find the visible coordinates from given coordinate.
     *
     * Explanation: The immediate UP/DOWN/LEFT/RIGHT tiles must be calculated.
     *
     * There could be max of 4 tiles, and min of 2 tiles (on corners).
     * Every found coordinate must be in the board limits.
     *
     * If the given coordinate is not in board, return a vector with size = 0. Order does NOT matter.
     *
     * Example:
     *
     * 01----
     * 02HH--
     * ------
     *
     * visibleCoordsFromCoord(Coordinate(0, 0)) == { (1, 0), (0, 1) }
     * visibleCoordsFromCoord(Coordinate(1, 1)) == { (1, 0), (2, 1), (1, 2), (0, 1) }
     * visibleCoordsFromCoord(Coordinate(-1, 0)) == { }
     *
     * @param coord The coordinate to search.
     * @return All coordinates visible.
     */
    std::vector<Coordinate> Board::visibleCoordsFromCoord(const Coordinate &coord) const{
        std::vector<Coordinate> list;
        if(!isCoordInBoard(coord)) return list;
        if(isCoordInBoard(coord + UP)) list.push_back(Coordinate(coord + UP));
        if(isCoordInBoard(coord + DOWN)) list.push_back(Coordinate(coord + DOWN));
        if(isCoordInBoard(coord + LEFT)) list.push_back(Coordinate(coord + LEFT));
        if(isCoordInBoard(coord + RIGHT)) list.push_back(Coordinate(coord + RIGHT));

        return list;
    }

    /**
     * Calculate the storm according to the currentRound.
     *
     * @param currentRound The current round being played.
     */
    void Board::updateStorm(uint currentRound){
        int width = Entity::stormWidthForRound(currentRound);

        if(!(boardSize  <=  2*width))
            storm = width;
    }

    std::vector<Player *>* Board::getPlayers() const
    {
        return players;
    }
