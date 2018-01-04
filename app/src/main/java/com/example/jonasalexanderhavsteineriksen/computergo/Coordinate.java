package com.example.jonasalexanderhavsteineriksen.computergo;

/** represents a position on a board */
public interface Coordinate {

    /** getter for the x value */
    int getX();

    /** getter for the y value */
    int getY();

    /** check whether this position is valid for the given board size */
    boolean checkBoundaries(int xSize, int ySize);

    /** move the position by dx to the right and by dy down */
    Coordinate shift(int dx, int dy);

}

