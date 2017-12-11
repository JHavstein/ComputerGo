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
