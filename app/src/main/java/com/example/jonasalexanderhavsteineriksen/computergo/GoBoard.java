package com.example.jonasalexanderhavsteineriksen.computergo;

/**
 * Created by jonasalexanderhavsteineriksen on 05/12/2017.
 */
public class GoBoard {

    /** 2-dimensional array representing the board
     * coordinates are counted from top-left (0,0) to bottom-right (size-1, size-1)
     * board[x][y] == 0   signifies free at position (x,y)
     * board[x][y] == i   for i > 0 signifies that Player i made a move on (x,y)
     */
    private int[][] board;

    /* State of the board before previous ove */
    private int[][] board1Before;

    /* State of the board before the move before the previous move */
    private int[][] board2before;

    /** size of the (quadratic) board */
    private int size;

    /** constructor for creating a copy of the board
     * not needed in Part 1 - can be viewed as an example
     */
    public GoBoard(GoBoard original) {
        this.size = original.size;
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                this.board[x][y] = original.board[x][y];
            }
        }
    }

    /** constructor for creating an empty board for a given number of players */
    public GoBoard() {
        this.size =  9; // hard coded board size - real size is 19 x 19
        this.board = new int[this.getSize()][this.getSize()];
        this.board1Before = new int[this.getSize()][this.getSize()];
        this.board2before = new int[this.getSize()][this.getSize()];
    }

    /** checks whether the board is free at the given position */
    public boolean isFree(Coordinate c) {
        return this.board[c.getX()][c.getY()] == 0;
        //this is added
    }

    /** returns the players that made a move on (x,y) or 0 if the position is free */
    public int getPlayer(Coordinate c) {
        return this.board[c.getX()][c.getY()];
    }
    //this is added

    /** record that a given player made a move at the given position
     * checks that the given positions is on the board
     * checks that the player number is valid
     */
    public void addMove(Coordinate c, int player) {
        updateBoards();
        if (c.checkBoundaries(this.size,this.size) && player > 0 && player <= this.size-1) {
            this.board[c.getX()][c.getY()] = player;
        }
        else {
            throw new IllegalArgumentException("Error");
        }
    }

    /** returns true if, and only if, there are no more free positions on the board */
    // Could be deleted?
    public boolean checkFull() {
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (this.board[x][y] == 0)
                    return false;
            }
        }
        return true;
    }

    /** returns 0 if no player has won (yet)
     * otherwise returns the number of the player that has three in a row
     */
    // should probably be deleted
    public int checkWinning(){
        //counters for the final number of stones the two players have on the board
        int counterPlayer1=0;
        int counterPlayer2=0;
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){
                if (this.board[j][i] == 1){
                    counterPlayer1++;
                }
                else if (this.board[j][i] == 2){
                    counterPlayer2++;
                }
                else{
                    ;
                }
            }
        }
        // player one wins
        if (counterPlayer1 > counterPlayer2){
            return 1;
        }
        // player two wins
        else if (counterPlayer2 > counterPlayer1){
            return 2;
        }
        // draw
        else{
            return 0;
        }
    }

    /** getter for size of the board */
    public int getSize() {
        return this.size;
    }

    /** pretty printing of the board
     * usefule for debugging purposes
     */
    public String toString() {
        String result = "";
        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                result += this.board[y][x]+" ";
            }
            result += "\n";
        }
        return result;
    }
    /* * Updates the two board that store the state of the board one and
    two moves ago.
     */
    private void updateBoards(){
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){
                this.board2before[j][i] = this.board1Before[j][i];
                this.board1Before[j][i] = this.board[j][i];
            }
        }
    }

    public int[][] getBoard(){
        return this.board;
    }

    public int[][] getBoard1Before(){
        return this.board1Before;
    }

    public int[][] getBoard2before(){
        return this.board2before;
    }

}