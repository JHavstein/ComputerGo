package com.example.jonasalexanderhavsteineriksen.computergo;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;

/** main class creating a board and the GUI
 * defines the game play
 */
public class GoGame implements Game {

    /** currently active player */
    public int currentPlayer;

    /** total number of players */
    public int numPlayers;

    /** the board we play on */
    public GoBoard board;

    /* Counts the number of moves so far */
    public int moveCounter;

    /** the gui for board games */
    public UserInterface ui;

    /** constructor that gets the number of players */
    public GoGame() {
        this.currentPlayer = 1;
        this.numPlayers = 2;
        this.board = new GoBoard();
        this.moveCounter = 0;
    }

    public void checkLiberty(){
        this.board.checkBoard();
    }

    @Override
    public String getTitle() {
        return "Computer Go";
    }

    @Override
    public void addMove(Coordinate pos) {
        // Increments move counter
        numberOfMoves();

        // boolean for whether the board is equal to the state two moves ago, i.e. two passes.
        boolean check = ((Arrays.deepEquals(this.board.getBoard(), this.board.getBoard1Before())) &&
                (Arrays.deepEquals(this.board.getBoard(), this.getBoard2Before())));
        if (this.board.getPlayer(pos) == this.currentPlayer) { //pass
            if (this.currentPlayer == this.numPlayers) {
                this.currentPlayer = 1;
             } else {
                this.currentPlayer++;
            }
        }
        else { // no pass - move is added
            this.board.addMove(pos, this.currentPlayer);
            if (this.currentPlayer == this.numPlayers) {
                this.currentPlayer = 1;
            } else {
                this.currentPlayer++;
            }
        }
            if (this.moveCounter > 2){
                if(check){ // two passes in a row - game ends
                    checkResult();
            }
        }
    }

    /* Checks th KO rule */
    public boolean checkKoRule(Coordinate pos) {
        if (this.moveCounter < 2) {
            return true;
        } else {
            // makes copy of the board with a move added
            int[][] temp = new int[this.getHorizontalSize()][this.getVerticalSize()];
            temp[pos.getX()][pos.getY()] = this.currentPlayer;
            for (int i = 0; i < this.getHorizontalSize(); i++) {
                for (int j = 0; j < this.getVerticalSize(); j++) {
                    Coordinate temp2 = new XYCoordinate(j, i);
                    temp[j][i] = this.board.getPlayer(temp2);
                }
            }
            if (Arrays.deepEquals(temp, this.board.getBoard1Before())) {
                return false;
            }
        }
            return true;
    }


    @Override
    public String getContent(Coordinate pos) {
        String result = "";
        int player = this.board.getPlayer(pos);
        if (player > 0) {
            result += player;
        }
        return result;
    }

    @Override
    public int getHorizontalSize() {
        return this.board.getSize();
    }

    @Override
    public int getVerticalSize() {
        return this.board.getSize();
    }

    @Override
    public void checkResult() {
        int winner = this.board.checkWinning();
        if (winner > 0) {
            this.ui.showResult("Player "+winner+" wins!");
        }
        else {
            this.ui.showResult("This is a DRAW!");
        }
    }

    private void numberOfMoves(){
        this.moveCounter++;
    }

    /** Checks recursively whether a given coordinate meets liberty rule
     * -1 indicates death */
    private int[][] findChain(Coordinate c) { // coordinate has free neighbour

        int[][] chain = new int[19][19]; // stores coordinates of chain
        boolean[][] mark = new boolean[19][19]; // marks visited coordinates
        int thisPlayer = this.board.getPlayer(c);

        // Makes four new neighbouring coordinates that will be basis of recursion
        Coordinate newRight = c.shift(1, 0);
        Coordinate newLeft = c.shift(-1, 0);
        Coordinate newUp = c.shift(0, 1);
        Coordinate newDown = c.shift(0, -1);

        // condition (at least one free neighbour) used in conditional further down.
        boolean hasLiberty =
                ((this.board.isFree(newRight)) ||
                        (this.board.isFree(newLeft)) ||
                        (this.board.isFree(newUp)) ||
                        (this.board.isFree(newDown)));

        // SPECIAL CASE
        // Checks for immidiate death when surrounded by enemies
        if ((this.board.getPlayer(newRight) != thisPlayer) &&
                (this.board.getPlayer(newLeft) != thisPlayer) &&
                (this.board.getPlayer(newUp) != thisPlayer) &&
                (this.board.getPlayer(newDown) != thisPlayer)) {
            chain[c.getX()][c.getY()] = -1; //stone dies
            return chain;
        }

        // BASE CASE 1
        // Coordinate has a free neighbour
        // Chain survives
        if (hasLiberty) {
            int[][] tmp = null;
            return tmp; // because then not a chain
        }
        // BASE CASE 2
        // Stone has no liberty AND
        // stones has no unmarked neighbours of the same color as itself
        // the end of the chain has been reached and chain dies!
        int[][] temp1 = null;
        int[][] temp2 = null;
        int[][] temp3 = null;
        int[][] temp4 = null;
        if (!hasLiberty) { //no liberty
            chain = addToChain(chain, c, -1); // -1 indicates (potential) death
            mark = addMark(c, mark);
            if ((mark[newRight.getX()][newRight.getY()] &&
                    (mark[newLeft.getX()][newLeft.getY()]) &&
                    (mark[newUp.getX()][newUp.getY()]) &&
                    (mark[newDown.getX()][newDown.getY()]))) {
                return chain;
            }
            if ((!mark[newRight.getX()][newRight.getY()]) ||
                    (!mark[newLeft.getX()][newLeft.getY()]) ||
                    (!mark[newUp.getX()][newUp.getY()]) ||
                    (!mark[newDown.getX()][newDown.getY()])) {

                // RECURSIVE STEPS
                // If neighbour stone is of same color AND
                // if that neighbour is unmarked
                if ((this.board.getPlayer(newRight) == thisPlayer) && (!mark[newRight.getX()][newRight.getY()])) {
                    temp1 = findChain(newRight);
                }
                if ((this.board.getPlayer(newLeft) == thisPlayer) && (!mark[newLeft.getX()][newLeft.getY()])) {
                    temp2 = findChain(newLeft);
                }
                if ((this.board.getPlayer(newUp) == thisPlayer) && (!mark[newUp.getX()][newUp.getY()])) {
                    temp3 = findChain(newUp);
                }
                if ((this.board.getPlayer(newDown) == thisPlayer) && (!mark[newDown.getX()][newDown.getY()])) {
                    temp4 = findChain(newDown);
                }
            }
        }
        // return
        if ((temp1 == null) || (temp2 == null) || (temp3 == null) || (temp4 == null)){
            int[][] ret = null;
            return null;
        }
        else{
            return removeDuplicates(chain);
        }
    }

    /** Adds boolean mark to coordinate when visited */
    private boolean[][] addMark(Coordinate c, boolean[][] markList){
        markList[c.getX()][c.getY()] = true;
        return markList;
    }

    /** Keeps list of coordinates that are connected in a chain */
    private int[][] addToChain(int[][] chain, Coordinate c, int value){
        chain[c.getX()][c.getY()] = value;
        return chain;
    }

    //not used
    private int[][] removeDuplicates(int[][] l){
        HashSet<Integer> set = new HashSet<Integer>();
        for (int i = 0; i<l[0].length; i++){
            for (int j = 0; j < l.length; j++){
                set.add(new Integer(l[j][i]));
            }
        }
        return (int[][]) set.toArray();
    }


    @Override
    public boolean isFree(Coordinate pos) {
        // if 0 then cell is free
        // if cell == this player then also counted as free, becaus this means that
        // the player wants to pass
        if ((this.board.getPlayer(pos) == 0) ||
                this.board.getPlayer(pos) == this.currentPlayer){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void setUserInterface(UserInterface ui) {
        this.ui = ui;

    }

    public String toString() {
        return "Board before Player "+this.currentPlayer+" of "+this.numPlayers+"'s turn:\n"+this.board.toString();
    }

    @Override
    public int[][] getBoard(){
        return this.board.getBoard();
    }

    public int[][] getBoard1Before(){
        return this.board.getBoard1Before();
    }

    public int[][] getBoard2Before(){
        return this.board.getBoard2before();
    }

    @Override
    public int numberOfPlayers(){
        return this.numPlayers;
    }

    public boolean isFull(){
        return this.board.checkFull();
    }

}
