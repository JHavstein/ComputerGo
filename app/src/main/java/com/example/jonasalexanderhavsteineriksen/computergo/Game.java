package com.example.jonasalexanderhavsteineriksen.computergo;


public interface Game {

    /** title of the game */
    String getTitle();

    void checkLiberty();

    /** horizontal size of the board */
    int getHorizontalSize();

    /** vertical size of the board */
    int getVerticalSize();

    /** string representation of the position
     * useful for user interface
     */
    String getContent(Coordinate pos);

    /** record a move on a given position */
    void addMove(Coordinate pos);

    /** check if some player wins or it is a draw */
    void checkResult();

    int getPlayer(Coordinate pos);

    /*Checks the KO rule*/
    boolean checkKoRule(Coordinate pos);


    /** provide a user interface to the game */
    void setUserInterface(UserInterface ui);

    void updateTemporaryBoards(Coordinate pos);
}
