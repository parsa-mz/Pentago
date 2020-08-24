import java.util.ArrayList;
import java.util.Arrays;


public class PentagoNode implements Comparable<PentagoNode> {
    private final char EMPTY = '.', WHITE = 'W', BLACK = 'B', TIE = 'T';
    private final ArrayList<Integer> QUADS = new ArrayList<>(Arrays.asList(1,2,3,4));
    private final String OCCUPIED = "\nThat space isn't available";
    private final int QUAD_DIM = 3;
    private final int BOARD_DIM = 6;
    private char[][] myQ1;
    private char[][] myQ2;
    private char[][] myQ3;
    private char[][] myQ4;
    private ArrayList<PentagoNode> myChildren;
    private int myValue;
    private int myDepth;
    private PentagoNode myParent;
    private Player myPlayer;
    private int win;
    private int bWin, wWin;
    private int maxValue, minValue;

    /**
     * Constructor.
     *
     * @param theDepth how deep in the tree is the node
     * @param parent who did this come from
     * @param player who's playing?
     */
    public PentagoNode(int theDepth, PentagoNode parent, Player player){
        myQ1 = new char[QUAD_DIM][QUAD_DIM];
        myQ2 = new char[QUAD_DIM][QUAD_DIM];
        myQ3 = new char[QUAD_DIM][QUAD_DIM];
        myQ4 = new char[QUAD_DIM][QUAD_DIM];
        if(parent == null){
            for(int i = 0; i < QUAD_DIM; i++){
                for(int j = 0; j < QUAD_DIM; j++){
                    myQ1[i][j] = EMPTY;
                    myQ2[i][j] = EMPTY;
                    myQ3[i][j] = EMPTY;
                    myQ4[i][j] = EMPTY;
                }
            }
        } else{
            myQ1 = makeCopy(parent.getQ1());
            myQ2 = makeCopy(parent.getQ2());
            myQ3 = makeCopy(parent.getQ3());
            myQ4 = makeCopy(parent.getQ4());
        }


        myDepth = theDepth;
        myParent = parent;
        myPlayer = player;
        myChildren = new ArrayList<>();
        maxValue = 0;
        minValue = 0;
        win = -1;
        bWin = 0;
        wWin = 0;

        findValue();
        myValue = maxValue + minValue;
    }

    /**
     * Set the player of the node.
     *
     * @param thePlayer who is playing
     */
    public void setPlayer(Player thePlayer){
        myPlayer = thePlayer;
    }

    /**
     * Add a child to this node.
     *
     * @param theChild the node to add to the list
     */
    public void addChild(PentagoNode theChild){
        myChildren.add(theChild);
    }

    /**
     * Get the children
     *
     * @return A list of children
     */
    public ArrayList<PentagoNode> getChildren(){
        return myChildren;
    }

    /**
     * Find out who produced this child!
     *
     * @return the parent node
     */
    public PentagoNode getParent(){
        return myParent;
    }

    /**
     * This node was adopted so decide who it belongs to.
     *
     * @param theParent the adopting node
     */
    public void setParent(PentagoNode theParent){
        myParent = theParent;
    }

    /**
     * What is this node worth?
     *
     * @return the overall value of the node
     */
    public int getValue(){
        return myValue;
    }

    /**
     * Who is using this node?
     * @return the player using the node
     */
    public Player getPlayer(){
        return myPlayer;
    }

    /**
     * How deep should this node be?
     *
     * @param theDepth depth level
     */
    public void setDepth(int theDepth){

    }

    /**
     * How deep is this node?
     *
     * @return the depth
     */
    public int getDepth(){
        return myDepth;
    }

    /**
     * Get QUAD 1
     * @return the 2d char array
     */
    public char[][] getQ1() {
        return myQ1;
    }

    /**
     * Get QUAD 2
     * @return the 2d char array
     */
    public char[][] getQ2() {
        return myQ2;
    }

    /**
     * Get QUAD 3
     * @return the 2d char array
     */
    public char[][] getQ3() {
        return myQ3;
    }

    /**
     * Get QUAD 4
     * @return the 2d char array
     */
    public char[][] getQ4() {
        return myQ4;
    }

    /**
     * Get the whole board
     * @return the 2d char array
     */
    public char[][] getState(){
        return bringItTogether();
    }

    /*
        Helper method that check a row to update the value of the node.
     */
    private ArrayList<Integer> checkRow(char[][] board, int row){
        ArrayList<Integer> wb = new ArrayList<>();
        int b = 0, w = 0;
        int bInRow = 1;
        int wInRow = 1;
        for(int i = 1; i < board.length; i++){
            char current = board[row][i], previous = board[row][i-1];
            if(current == previous && current != EMPTY){
                if(current == BLACK){
                    bInRow++;
                    if(bInRow == 3){
                        b += 100;
                    }else if(bInRow == 4){
                        b += 900;
                    } else if(bInRow == 5){
                        b += 99000;
                        bWin++;
                    }
                }else{
                    wInRow++;
                    if(wInRow == 3){
                        w += 100;
                    }else if(wInRow == 4){
                        w += 900;
                    }else if(wInRow == 5){
                        w += 99000;
                        wWin++;
                    }
                }
            }else{
                if(current == BLACK){
                    bInRow = 1;
                    wInRow = 0;
                }else if(current == WHITE){
                    wInRow = 1;
                    bInRow = 0;
                }else{
                    bInRow = 0;
                    wInRow = 0;
                }
            }
        }

        wb.add(w);
        wb.add(b);
        return wb;
    }

    /*
        Helper method that check a column to update the value of the node.
     */
    private ArrayList<Integer> checkColumn(char[][] board, int column){
        ArrayList<Integer> wb = new ArrayList<>();
        int b = 0, w = 0;
        int bInRow = 1;
        int wInRow = 1;
        for(int i = 1; i < board.length; i++){
            char current = board[i][column], previous = board[i-1][column];
            if(current == previous && current != EMPTY){
                if(current == BLACK){
                    bInRow++;
                    if(bInRow == 3){
                        b += 100;
                    }else if(bInRow == 4){
                        b += 900;
                    } else if(bInRow == 5){
                        b += 99000;
                        bWin++;
                    }
                }else{
                    wInRow++;
                    if(wInRow == 3){
                        w += 100;
                    }else if(wInRow == 4){
                        w += 900;
                    }else if(wInRow == 5){
                        w += 99000;
                        wWin++;
                    }
                }
            }else{
                if(current == BLACK){
                    bInRow = 1;
                    wInRow = 0;
                }else if(current == WHITE){
                    wInRow = 1;
                    bInRow = 0;
                }else{
                    bInRow = 0;
                    wInRow = 0;
                }
            }
        }
        wb.add(w);
        wb.add(b);
        return  wb;
    }

    /*
        Helper method that check a diagonal to update the value of the node.
     */
    private ArrayList<Integer> checkDiag(char[][] board, int start, int stop, int offset){
        ArrayList<Integer> wb = new ArrayList<>();
        int b = 0, w = 0;
        int bInRow = 1;
        int wInRow = 1;
        for(int i = start; i<=stop; i++){
            char current = board[i + (1-offset)][i], previous = board[i - offset][i-1];
            if(current == previous && current != EMPTY){
                if(current == BLACK){
                    bInRow++;
                    if(bInRow == 3){
                        b += 100;
                    }else if(bInRow == 4){
                        b += 900;
                    } else if(bInRow == 5){
                        b += 99000;
                        bWin++;
                    }
                }else{
                    wInRow++;
                    if(wInRow == 3){
                        w += 100;
                    }else if(wInRow == 4){
                        w += 900;
                    }else if(wInRow == 5){
                        w += 99000;
                        wWin++;
                    }
                }
            }else{
                if(current == BLACK){
                    bInRow = 1;
                    wInRow = 0;
                }else if(current == WHITE){
                    wInRow = 1;
                    bInRow = 0;
                }else{
                    bInRow = 0;
                    wInRow = 0;
                }
            }
        }
        wb.add(w);
        wb.add(b);
        return wb;
    }

    /*
        Helper method that check an anti diagonal to update the value of the node.
     */
    private ArrayList<Integer> checkAnti(char[][] board, int start, int stop, int offset){
        ArrayList<Integer> wb = new ArrayList<>();
        int b = 0, w =0;
        int bInRow = 1;
        int wInRow = 1;
        for(int i = start; i <= stop; i++){
            char current = board[offset - i][i], previous = board[offset+1-i][i-1];
            if(current == previous && current != EMPTY){
                if(current == BLACK){
                    bInRow++;
                    if(bInRow == 3){
                        b += 100;
                    }else if(bInRow == 4){
                        b += 900;
                    } else if(bInRow == 5){
                        b += 99000;
                        bWin++;
                    }
                }else{
                    wInRow++;
                    if(wInRow == 3){
                        w += 100;
                    }else if(wInRow == 4){
                        w += 900;
                    }else if(wInRow == 5){
                        w += 99000;
                        wWin++;
                    }
                }
            }else{
                if(current == BLACK){
                    bInRow = 1;
                    wInRow = 0;
                }else if(current == WHITE){
                    wInRow = 1;
                    bInRow = 0;
                }else{
                    bInRow = 0;
                    wInRow = 0;
                }
            }
        }

        wb.add(w);
        wb.add(b);
        return wb;
    }

    private ArrayList<Integer> checkMid(char[][] board, int quad){
        ArrayList<Integer> wb = new ArrayList<>();
        int b = 0;
        int w = 0;
        if(quad == 1){
            if(board[1][1] == BLACK){
                b += 5;
            }
            if(board[1][1] == WHITE){
                w += 5;
            }
        }else if (quad == 2){
            if(board[1][4] == BLACK){
                b += 5;
            }
            if(board[1][4] == WHITE){
                w += 5;
            }
        }else if(quad == 3){
            if(board[4][1] == BLACK){
                b += 5;
            }
            if(board[4][1] == WHITE){
                w += 5;
            }
        } else if(quad == 4){
            if(board[4][4] == BLACK){
                b += 5;
            }
            if(board[4][4] == WHITE){
                w += 5;
            }
        }

        wb.add(w);
        wb.add(b);

        return wb;
    }

    /*
        Check all 18 ways a player can win for values, 10 for 3 in a row, 20 for 4 in a row and 100 for 5 in a row
     */
    private void findValue(){
        int b = 0, w = 0;
        char[][] wholeBoard = bringItTogether();

        /*
            QUAD 1 Center
         */
        ArrayList<Integer> q1Center = checkMid(wholeBoard, 1);
        b += q1Center.get(1);
        w += q1Center.get(0);

        /*
            QUAD 2 CENTER
         */
        ArrayList<Integer> q2Center = checkMid(wholeBoard, 2);
        b += q2Center.get(1);
        w += q2Center.get(0);

        /*
            QUAD 3 CENTER
         */
        ArrayList<Integer> q3Center = checkMid(wholeBoard, 3);
        b += q3Center.get(1);
        w += q3Center.get(0);

        /*
            QUAD 4 CENTER
         */
        ArrayList<Integer> q4Center = checkMid(wholeBoard, 4);
        b += q4Center.get(1);
        w += q4Center.get(0);

        /*
            ROW 0
         */
        ArrayList<Integer> row0 = checkRow(wholeBoard, 0);
        b+= row0.get(1);
        w+= row0.get(0);

        /*
            ROW 1
         */
        ArrayList<Integer> row1 = checkRow(wholeBoard, 1);
        b += row1.get(1);
        w += row1.get(0);

        /*
            ROW 2
         */
        ArrayList<Integer> row2 = checkRow(wholeBoard, 2);
        b += row2.get(1);
        w += row2.get(0);

        /*
            ROW 3
         */
        ArrayList<Integer> row3 = checkRow(wholeBoard, 3);
        b+= row3.get(1);
        w+= row3.get(0);

        /*
            ROW 4
         */
        ArrayList<Integer> row4 = checkRow(wholeBoard, 4);
        b+= row4.get(1);
        w+= row4.get(0);

        /*
            ROW 5
         */
        ArrayList<Integer> row5 = checkRow(wholeBoard, 5);
        b += row5.get(1);
        w += row5.get(0);

        /*
            COLUMN 0
         */
        ArrayList<Integer> col0 = checkColumn(wholeBoard, 0);
        b += col0.get(1);
        w += col0.get(0);

        /*
            COLUMN 1
         */
        ArrayList<Integer> col1 = checkColumn(wholeBoard, 1);
        b += col1.get(1);
        w += col1.get(0);

        /*
            COLUMN 2
         */
        ArrayList<Integer> col2 = checkColumn(wholeBoard, 2);
        b += col2.get(1);
        w += col2.get(0);

        /*
            COLUMN 3
         */
        ArrayList<Integer> col3 = checkColumn(wholeBoard, 3);
        b += col3.get(1);
        w += col3.get(0);

        /*
            COLUMN 4
         */
        ArrayList<Integer> col4 = checkColumn(wholeBoard, 4);
        b += col4.get(1);
        w += col4.get(0);

        /*
            COLUMN 5
         */
        ArrayList<Integer> col5 = checkColumn(wholeBoard, 5);
        b += col5.get(1);
        w += col5.get(0);

        /*
            MAIN DIAGONAL
         */
        ArrayList<Integer> diag0 = checkDiag(wholeBoard, 1, 5, 1);
        b += diag0.get(1);
        w += diag0.get(0);

        /*
            UPPER DIAGONAL
         */
        ArrayList<Integer> diag1 = checkDiag(wholeBoard, 2, 5, 2);
        b += diag1.get(1);
        w += diag1.get(0);

        /*
            LOWER DIAGONAL
         */
        ArrayList<Integer> diag2 = checkDiag(wholeBoard, 1, 4, 0);
        b += diag2.get(1);
        w += diag2.get(0);

        /*
            MAIN ANTI DIAGONAL
         */
        ArrayList<Integer> anti0 = checkAnti(wholeBoard, 1, 5, 5);
        b += anti0.get(1);
        w += anti0.get(0);

        /*
            UPPER ANTI DIAGONAL
         */
        ArrayList<Integer> anti1 = checkAnti(wholeBoard, 1, 4, 4);
        b += anti1.get(1);
        w += anti1.get(0);

        /*
            Lower Anti-diagonal
         */
        ArrayList<Integer> anti2 = checkAnti(wholeBoard, 2, 5, 6);
        b += anti2.get(1);
        w += anti2.get(0);

        if(myPlayer.isMax()){//current player is a max
            if(myPlayer.getPiece() == BLACK){
                maxValue = b;
                minValue = w;
            }else{
                minValue = b;
                maxValue = w;
            }
        }else{//current player is min
            if(myPlayer.getPiece() == BLACK){
                minValue = b;
                maxValue = w;
            }else{
                maxValue = b;
                minValue = w;
            }
        }

        minValue = -minValue;
        myValue = maxValue + minValue;
        int x = 0;
    }

    /**
     * Checks to see if there is a win state in this node.
     *
     * @return -1 if game not done, 0 if game tie, 1 if White wins 2 if Black wins
     */
    public int winState() {
        if(bWin > wWin){
            win = 2;
        }else if(bWin < wWin) {
            win = 1;
        }else if(bWin > 0 && wWin > 0 && bWin == wWin) {
            win = 0;
        }else {
            win = -1;
        }
        return win;
    }

    /**
     * Rotates the given quadrant Left.
     *
     * @param quad the quad to rotate
     */
    public void rotateLeft(int quad){
        if(QUADS.contains(quad)){
            if(quad == 1){
                rotateL(myQ1);
            }else if(quad == 2){
                rotateL(myQ2);
            } else if(quad == 3){
                rotateL(myQ3);
            } else if(quad == 4){
                rotateL(myQ4);
            }
        } else {
            System.out.println("");
        }
        findValue();
    }


    /**
     * Rotates a quad right.
     *
     * @param quad the quad to rotate
     */
    public void rotateRight(int quad){
        if(QUADS.contains(quad)){
            if(quad == 1){
                rotateR(myQ1);
            }else if(quad == 2){
                rotateR(myQ2);
            } else if(quad == 3){
                rotateR(myQ3);
            } else if(quad == 4){
                rotateR(myQ4);
            }
        } else {
            System.out.println("");
        }
        findValue();
    }

    /*
        Helper method to rotate a quad left.
     */
    private void rotateL(char[][] quad){
        char[][] temp = makeCopy(quad);
        for(int i = 0 ; i < quad.length; i++){
            for(int j = 0; j < quad.length; j++){
                quad[i][j] = temp[j][quad.length - i - 1];
            }
        }
    }

    /*
        Helper method to rotate a quad right.
     */
    private void rotateR(char[][] quad){
        char[][] temp = makeCopy(quad);
        for(int i = 0 ; i < quad.length; i++){
            for(int j = 0; j < quad.length; j++){
                quad[i][j] = temp[quad.length - j - 1][i];
            }
        }
    }

    /*
        Helper method to make a copy of an 2D array
     */
    private char[][] makeCopy(char[][] toCopy){
        char[][] copy = new char[toCopy.length][toCopy.length];
        for(int i = 0; i < toCopy.length; i++){
            for(int j = 0; j < toCopy.length; j++){
                copy[i][j] = toCopy[i][j];
            }
        }
        return copy;
    }

    /**
     * A method that will update the position of a spot on the board.
     *
     * @param quadrant which sub matrix to change
     * @param row which row in that sub matrix to change
     * @param col which column to change
     * @param b what will the index be changed to
     */
    public boolean changeSpace(int quadrant, int row, int col, char b){
        boolean toReturn = false;
        if(quadrant >= 1 && quadrant <=4 && row >= 0 && row <= 2 && col >=0 && col <= 2){
            if(QUADS.contains(quadrant)){
                if(quadrant == 1){
                    if(myQ1[row][col] == EMPTY){
                        myQ1[row][col] = b;
                        toReturn = true;
                    }else {
                        System.out.println(OCCUPIED);
                    }
                } else if(quadrant == 2){
                    if(myQ2[row][col] == EMPTY){
                        myQ2[row][col] = b;
                        toReturn = true;
                    }else {
                        System.out.println(OCCUPIED);
                    }
                } else if(quadrant == 3){
                    if(myQ3[row][col] == EMPTY){
                        myQ3[row][col] = b;
                        toReturn = true;
                    }else {
                        System.out.println(OCCUPIED);
                    }
                } else if(quadrant == 4){
                    if(myQ4[row][col] == EMPTY){
                        myQ4[row][col] = b;
                        toReturn = true;
                    }else {
                        System.out.println(OCCUPIED);
                        toReturn = false;
                    }
                }

            }
        }
        findValue();
        return toReturn;
    }

    /**
     * Checks to see if a node is a leaf in the tree.
     *
     * @return true if a leaf
     */
    public boolean isLeaf(){
        return myChildren.size() == 0;
    }

    /*
        Helper method for combining the Quads into one 2d Array
     */
    private char[][] bringItTogether(){
        char[][] wholeBoard = new char[BOARD_DIM][BOARD_DIM];
        for(int i = 0; i < BOARD_DIM; i++){
            for(int j = 0; j < BOARD_DIM; j++){
                if(i < BOARD_DIM/2 && j < BOARD_DIM/2){
                    wholeBoard[i][j] = myQ1[i][j];
                }
                if(i < BOARD_DIM/2 && j >= BOARD_DIM/2){
                    wholeBoard[i][j] = myQ2[i][j-QUAD_DIM];
                }
                if(i >= BOARD_DIM/2 && j < BOARD_DIM/2){
                    wholeBoard[i][j] = myQ3[i - QUAD_DIM][j];
                }
                if(i >= BOARD_DIM/2 && j >= BOARD_DIM/2){
                    wholeBoard[i][j] = myQ4[i - QUAD_DIM][j - QUAD_DIM];
                }

            }
        }

        return wholeBoard;
    }

    /**
     * Overridden toString that prints out the node.
     *
     * @return a string with the state of the board printed out
     */
    @Override
    public String toString(){
        char[][] wholeBoard = bringItTogether();

        String divider = "+---------+---------+\n";
        StringBuilder output = new StringBuilder(divider);
        for(int i = 0; i < BOARD_DIM; i++){
            if (i == 3)
                output.append(divider);

            for(int j = 0; j< BOARD_DIM; j++){
                if(j == 0 || j == BOARD_DIM/2) //Add vertical line
                    output.append("|");

                output.append(" " + wholeBoard[i][j] + " ");

                if(j == BOARD_DIM - 1)
                    output.append("|\n");
            }
        }
        output.append(divider);
        return output.toString();
   }



    /**
     * Overriden equals method
     * @param o object to be compared
     * @return true if equals and false if not equals
     */
   @Override
    public boolean equals(Object o){
       return this.toString().equals(o.toString());
   }

    /**
     * Overridden compareTo method, based on the path cost of the nodes.
     *
     * @param o object to be compared with
     * @return -1 if this node is less than o, 0 if equal and 1 if this node is graeater than o
     */
    @Override
    public int compareTo(PentagoNode o) {
        return Integer.compare(myValue, o.getValue());
    }
}
