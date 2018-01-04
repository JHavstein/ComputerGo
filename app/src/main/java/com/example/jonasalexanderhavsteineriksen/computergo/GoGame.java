package com.example.jonasalexanderhavsteineriksen.computergo;

import java.util.Arrays;

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

    /* Counts the number of consecutive passes */
    public int passCounter;

    /** the gui for board games */
    public UserInterface ui;

    /** constructor that gets the number of players */
    public GoGame() {
        this.currentPlayer = 1;
        this.numPlayers = 2;
        this.board = new GoBoard();
        this.moveCounter = 0;
        this.passCounter = 0;
    }

    public void checkLiberty(){
        this.board.checkBoard(this.currentPlayer);
    }

    @Override
    public String getTitle() {
        return "Computer Go";
    }

    @Override
    public void addMove(Coordinate pos) {
        // Increments move counter
        this.moveCounter++;

        if (this.board.getPlayer(pos) != 0) { //pass
            if (this.currentPlayer == this.numPlayers) {
                this.currentPlayer = 1;
             } else {
                this.currentPlayer++;
            }
            this.passCounter++; // incrementing pass counter
        }
        else { // no pass - move is added
            this.board.addMove(pos, this.currentPlayer);
            this.passCounter = 0; // resetting pass counter
            if (this.currentPlayer == this.numPlayers) {
                this.currentPlayer = 1;
            } else {
                this.currentPlayer++;
            }
        }
        // If two consecutive passes:
        // GAME IS OVER
        if (this.passCounter >= 2){
            checkResult();
            }
        }

    public void updateTemporaryBoards(Coordinate pos){
        this.board.updateBoards();
    }

    /* Checks th KO rule */
    public boolean checkKoRule(Coordinate pos) {
        if (this.moveCounter < 2) {
            return true;
        } else {
            // makes copy of the board with a move added
            int[][] temp = new int[this.getHorizontalSize()][this.getVerticalSize()];
            for (int i = 0; i < this.getHorizontalSize(); i++) {
                for (int j = 0; j < this.getVerticalSize(); j++) {
                    Coordinate temp2 = new XYCoordinate(j, i);
                    if (this.board.getPlayer(temp2) == this.currentPlayer){
                        temp[j][i] = this.board.getPlayer(temp2);
                    }
                    else{
                        temp[j][i] = 0;
                    }
                }
            }
            temp[pos.getX()][pos.getY()] = this.currentPlayer;
            if (Arrays.deepEquals(temp, this.board.getBoard2before(this.currentPlayer))) {
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
        else{
            this.ui.showResult("This is a DRAW!");
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

    public int getPlayer(Coordinate pos) {
        return this.board.getPlayer(pos);
    }

}
