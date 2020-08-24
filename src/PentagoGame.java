import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Parsa Mazaheri
 * @version 2.2
 */

public class PentagoGame {
    static final String CHOICE = "what's your move? [ex:1/4 2R]";
     //"ex: 1/4 2R to place piece in Block: 1 Spot: 4 and rotate Block 2 Right";
    static final String PLAY_NUM = "HOW MANY PLAYERS? [1 or 2]";
    static final String FIRST_NAME = "Player 1 Name: ";
    static final String SECOND_NAME = "Player 2 Name: ";
    static final String TOKEN_COLOR = "First Player Token Color [W or B]: ";
    static final String WHICH_FIRST = "Which player should go first? [1 or 2]";
    static final String AI_NAME = "Catrina AI";
    static final char BLACK = 'B', WHITE = 'W', LEFT = 'L', RIGHT ='R', ALPHA = 'A', MINMAX = 'M';
    static final int TREE_DEPTH  = 3;
    static final String WON = " WON!";
    static final String WELCOME =
              "****************************************\n"
            + "*          WELCOME TO PENTAGO!         *\n"
            + "****************************************\n";


    public static void main(String[] args) {
        /* A HashMap that stores coordinates for spots within quads*/
        final HashMap<Integer, ArrayList<Integer>> spotToArray = new HashMap<>();
        spotToArray.put(1, new ArrayList<>(Arrays.asList(0,0)));
        spotToArray.put(2, new ArrayList<>(Arrays.asList(0,1)));
        spotToArray.put(3, new ArrayList<>(Arrays.asList(0,2)));
        spotToArray.put(4, new ArrayList<>(Arrays.asList(1,0)));
        spotToArray.put(5, new ArrayList<>(Arrays.asList(1,1)));
        spotToArray.put(6, new ArrayList<>(Arrays.asList(1,2)));
        spotToArray.put(7, new ArrayList<>(Arrays.asList(2,0)));
        spotToArray.put(8, new ArrayList<>(Arrays.asList(2,1)));
        spotToArray.put(9, new ArrayList<>(Arrays.asList(2,2)));

        Scanner scanner = new Scanner(System.in);
        int player = 1; //controls which player goes first
        Player p1, p2;

        Board board;
        String p1Name = "", p2Name = "";
        boolean p2AI = true, go = false;
        int pCount = 0, first = 0;
        char player1Token = ' ';
        char player2Token = ' ';

        System.out.println(WELCOME);

        // Player Count
        System.out.println(PLAY_NUM);
        while(!go){
            int numPlayers = scanner.nextInt();
            if(numPlayers == 1 || numPlayers == 2){
                pCount = numPlayers;
                go = true;
            } else {
                System.out.println("Only 1 or 2 players. Try Again");
                System.out.println(PLAY_NUM);
            }
        }

        // Player 1 name
        go = false;
        System.out.println(FIRST_NAME);
        String line = scanner.next();
        while(!go){
            if(line.length() > 0){
                p1Name = line;
                go = true;
            } else {
                System.out.println("You have to enter a name");
                System.out.println(FIRST_NAME);
                line = scanner.nextLine();
            }
        }

        // Player 1 W/B
        go = false;
        System.out.println(TOKEN_COLOR);
        line = scanner.next().toUpperCase();
        while(!go){
            if(line.length() == 1 && line.charAt(0) == BLACK){
                player1Token = BLACK;
                player2Token = WHITE;
                go = true;
            }else if(line.length() == 1 && line.charAt(0) == WHITE) {
                player1Token = WHITE;
                player2Token = BLACK;
                go = true;
            }else{
                System.out.println("The choices are B or W");
                System.out.println(TOKEN_COLOR);
                line = scanner.nextLine().toUpperCase();
            }
        }

        // player 2 name / AI name
        go = false;
        if(pCount == 2){ // if the game is 2 player
            System.out.println(SECOND_NAME);
            line = scanner.next();
            p2AI = false; // there will be no AI
            while(!go) {
                if(line.length() > 0){
                    p2Name = line;
                    go = true;
                } else {
                    System.out.println("Enter a valid name.");
                    System.out.println(SECOND_NAME);
                    line = scanner.nextLine();
                }
            }

        } else {
            p2Name = AI_NAME;
        }

        // Which one goes first
        go = false;
        System.out.println(WHICH_FIRST);
        first = scanner.nextInt();
        while(!go){

            if(first == 1 || first == 2){
                go = true;
            }else{
                System.out.println("The only options are 1 or 2.");
                System.out.println(WHICH_FIRST);
                first = scanner.nextInt();
            }
        }

        go = false;
        player = first;//which player will go first


        //assign max and min player
        if(player == 1){
            p1 = new Player(false, player1Token, p1Name, true);
            p2 = new Player(p2AI, player2Token, p2Name, false);
            board = new Board(p1, p2);
        }else{
            p1 = new Player(false, player1Token, p1Name, false);
            p2 = new Player(p2AI, player2Token, p2Name, true);
            board = new Board(p2, p1);
        }

        boolean gameDone = false;

        while(!gameDone){
            // Players turn cycle
            Player current;
            if(player % 2 == 0)
                current = p2;
            else
                current = p1;

            board.getGameState().setPlayer(current);

            if(current.isAI()){
                System.out.println(current.getName()+"'s turn: ");

                ArrayList<char[][]> quads = new ArrayList<>();
                quads.add(board.getGameState().getQ1());
                quads.add(board.getGameState().getQ2());
                quads.add(board.getGameState().getQ3());
                quads.add(board.getGameState().getQ4());

                // Alpha Beta purning
                PentagoNode temp = board.getGameState(); // zakhire halat alan
                PentagoNode newNode = board.alphaBetaPrune(temp, TREE_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, current, MINMAX);
                PentagoNode currentNode = newNode;
                while(!currentNode.getParent().equals(temp) && currentNode.getParent()!= null){
                    currentNode = currentNode.getParent();
                }
                board.setGameState(currentNode);
                board.setExpandedNodes(0);

                System.out.println(board.getGameState().toString());

                int result = board.getGameState().winState();
                if(result != -1){
                    gameDone = true;
                    winCheck(current, result, p1, p2);
                }

                if(!gameDone){
                    temp = board.getGameState();
                    newNode = board.alphaBetaPrune(board.getGameState(), TREE_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, current, RIGHT);

                    currentNode = newNode;
                    while (!currentNode.getParent().equals(temp) && currentNode.getParent() != null){
                        currentNode = currentNode.getParent();
                    }
                    board.setGameState(currentNode);
                    board.setExpandedNodes(0);

                    System.out.println("After Rotation:\n"+board.getGameState().toString());

                    result = board.getGameState().winState();
                    if(result != -1){
                        gameDone = true;
                        winCheck(current, result, p1, p2);
                    }
                }
                board.setExpandedNodes(0);

            } else { // Player's turn
                System.out.println("CURRENT STATE:\n"+board.getGameState().toString());
                System.out.println(current.getName()+"'s turn: ");
                //System.out.println(CHOICE);
                line = scanner.nextLine();
                int spotQuad = 0;
                int spot = 0;
                int rotateQuad = 0;
                char direction = ' ';

                while(!go){
                    boolean parse = false, validDir = false, innerGo = false;
                    while(!innerGo){
                        if(line.length() == 6){
                            parse = isParseable(String.valueOf(line.charAt(0)))
                                    && isParseable(String.valueOf(line.charAt(2)))
                                    && isParseable(String.valueOf(line.charAt(4)));
                            validDir = line.toUpperCase().charAt(5) == LEFT || line.toUpperCase().charAt(5) == RIGHT;
                        }
                        boolean inRange = false;
                        if(parse){
                            int one = Integer.parseInt(String.valueOf(line.charAt(0)));
                            int two = Integer.parseInt(String.valueOf(line.charAt(2)));
                            int three = Integer.parseInt(String.valueOf(line.charAt(4)));
                            inRange = one > 0 && one < 5 && two > 0 && two < 10 && three > 0 && three < 5;
                        }

                        if(line.length() == 6 && line.contains("/") && line.contains(" ") && parse && inRange && validDir){
                            spotQuad = Integer.parseInt(String.valueOf(line.charAt(0)));
                            spot = Integer.parseInt(String.valueOf(line.charAt(2)));
                            rotateQuad = Integer.parseInt(String.valueOf(line.charAt(4)));
                            direction = line.toUpperCase().charAt(5);
                            innerGo = true;
                        }else{
                            //System.out.println("wrong input, please try again");
                            System.out.println(CHOICE);
                            line = scanner.nextLine();
                        }
                    }

                    ArrayList<Integer> coords = spotToArray.get(spot);
                    if(!board.getGameState().changeSpace(spotQuad, coords.get(0), coords.get(1), current.getPiece())){
                        System.out.println("\nTry again  " + current.getName());
                        System.out.println(board.getGameState().toString());

                        System.out.println(CHOICE);
                        line = scanner.nextLine();

                    }else{
                        go = true;
                    }
                }

                go = false;
                System.out.println(board.getGameState().toString());
                int result = board.getGameState().winState();
                if(result != -1){
                    gameDone = true;
                    winCheck(current,result,p1, p2);
                }
                if(!gameDone){ //only enter if the game is still going after player's move
                    if(direction == LEFT){
                        board.getGameState().rotateLeft(rotateQuad);
                    }else if(direction == RIGHT){
                        board.getGameState().rotateRight(rotateQuad);
                    }
                    System.out.println("After rotation:\n"+ board.getGameState().toString());
                    result = board.getGameState().winState();
                    if(result != -1){
                        gameDone = true;
                        winCheck(current,result,p1, p2);
                    }
                }
            }
            player++;
        }
    }


    // Helper method to check is a string can be parsed into an Integer.
    static boolean isParseable(String toParse){
        boolean p = true;
        try{
            Integer.parseInt(toParse);
        }catch (NumberFormatException e){
            p = false;
        }
        return p;
    }

    // helper method for print out winner
    static void winCheck(Player current, int result, Player p1, Player p2){
        if(result == 0){
            //tie game
            System.out.println("THE GAME'S A TIE!");
        } else if(result == 1){
            //white won
            if(current.getPiece() == WHITE){
                System.out.println(current.getName() + WON);
            }else{
                if(current.equals(p1)){
                    System.out.println(p2.getName() + WON);
                }else{
                    System.out.println(p1.getName() + WON);
                }
            }
        } else if(result == 2){
            // black win
            if(current.getPiece() == BLACK){
                System.out.println(current.getName() + WON);
            }else{
                if(current.equals(p1)){
                    System.out.println(p2.getName() + WON);
                }else{
                    System.out.println(p1.getName() + WON);
                }
            }
        }
    }
}
