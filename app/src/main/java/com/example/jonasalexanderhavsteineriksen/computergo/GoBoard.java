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

    /* State of player 1's pieces on the board before the the previous move */
    private int[][] board1BeforePlayer1;

    /* State of player 1's pieces on the board before the move before the previous move */
    private int[][] board2beforePlayer1;

    /* State of player 2's pieces on the board before the the previous move */
    private int[][] board1BeforePlayer2;

    /* State of player 2's pieces on the board before the the previous move */
    private int[][] board2beforePlayer2;

    /** size of the (quadratic) board */
    private int size;

    private int[][] chain;

    private boolean[][] mark;

    private boolean liberty;

    private int player;

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
        this.size =  5; // hard coded board size - real size is 19 x 19
        this.board = new int[this.getSize()][this.getSize()];
        this.board1BeforePlayer1 = new int[this.getSize()][this.getSize()];
        this.board2beforePlayer1 = new int[this.getSize()][this.getSize()];
        this.board1BeforePlayer2 = new int[this.getSize()][this.getSize()];
        this.board2beforePlayer2 = new int[this.getSize()][this.getSize()];
        this.chain = new int[this.size][this.size];
        this.mark = new boolean[this.size][this.size];
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
        //updateBoards();
        if (c.checkBoundaries(this.size-1,this.size-1) && player > 0 && player <= this.size-1) {
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
        int counterPlayer1 = 0;
        int counterPlayer2 = 0;
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){
                if (this.board[j][i] == 1){
                    counterPlayer1++;
                }
                else if (this.board[j][i] == 2){
                    counterPlayer2++;
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

    public void checkBoard(int CurrentPlayer) {
        // Iterates through the board, finds and captures chains.
        if (CurrentPlayer == 1) {                         // check currentplayer
            for (player = 1; player < 3; player++) {
                for (int x = 0; x < this.size; x++) {
                    for (int y = 0; y < this.size; y++) {
                        Coordinate start = new XYCoordinate(x, y);
                        if (!isFree(start)) {                    // check if the position is not free
                            this.liberty = false;
                            int[][] tmp = new int[this.size][this.size];
                            tmp = findChain(start, player, tmp); // find chain based on the player in the start position
                            if (this.liberty == false) {         // if the chain has no liberties the chain is captured
                                capture(player, tmp);
                            }
                        }
                    }
                }
            }
        } else if (CurrentPlayer == 2) {
            for (player = 2; player > 0; player--) {
                for (int x = 0; x < this.size; x++) {
                    for (int y = 0; y < this.size; y++) {
                        Coordinate start = new XYCoordinate(x, y);
                        if (!isFree(start)) {                    // check if the position is not free
                            this.liberty = false;
                            int[][] tmp = new int[this.size][this.size];
                            tmp = findChain(start, player, tmp); // find chain based on the player in the start position
                            if (this.liberty == false) {         // if the chain has no liberties the chain is captured
                                capture(player, tmp);
                            }
                        }
                    }
                }
            }
        }
        this.chain = new int[this.size][this.size];
    }

    /** Method for finding a connected chain of a given player */
    public int[][] findChain(Coordinate start, int player, int[][] tmp) {
        // Get the coordinates from the start position, if the position is on the board.
        if (start.checkBoundaries(this.size-1, this.size - 1)) {
            int x = start.getX();
            int y = start.getY();
            // Check if this position is on the board, if it has the expected player and has not been visited before
            if (this.board[x][y] == player && this.chain[x][y] == 0) {
                this.chain[x][y] = player;  // save the position
                tmp[x][y] = player;         // save the position
                // Check the neighbours in the orthogonally-adjacent points
                findChain(start.shift(1, 0), player, tmp);
                findChain(start.shift(-1, 0), player, tmp);
                findChain(start.shift(0, 1), player, tmp);
                findChain(start.shift(0, -1), player, tmp);
            } else if (this.board[x][y] == 0) {  // check if the chain has a liberty
                this.liberty = true;
            }
        }
        return tmp;
    }

    /** Method for capturing chains */
    public void capture(int player, int[][] tmp) {
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (tmp[x][y] == player) {
                    this.board[x][y] = 0;
                }
            }
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

    public void updateBoards(){
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){

                // temporary boards reflecting player 1's position
                this.board2beforePlayer1[j][i] = (this.board1BeforePlayer1[j][i] == 1) ? 1 : 0;
                this.board1BeforePlayer1[j][i] = (this.board[j][i] == 1) ? 1 : 0; // because player 1

                // temporary boards reflecting player 2's position
                this.board2beforePlayer2[j][i] = (this.board1BeforePlayer2[j][i] == 2) ? 2 : 0;
                this.board1BeforePlayer2[j][i] = (this.board[j][i] == 2) ? 2 : 0; // because player 2

            }
        }
    }

    public int[][] getBoard(){
        return this.board;
    }

    public int[][] getBoard1Before(int player){
        if (player == 1){
            return this.board1BeforePlayer1;
        }
        else{
            return this.board1BeforePlayer2;
        }
    }

    public int[][] getBoard2before(int player){
        if (player == 1){
            return this.board2beforePlayer1;
        }
        else{
            return this.board2beforePlayer2;
        }
    }

}