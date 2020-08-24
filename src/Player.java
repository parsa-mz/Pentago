
public class Player {
    private boolean myAI;
    private char myPiece;
    private String myName;
    private boolean max;

    /**
     * Constructor for Player.
     *
     * @param theAI is it a human or a death robot?
     */
    public Player(boolean theAI, char thePiece, String name, boolean theMax){
        myAI = theAI;
        myPiece = thePiece;
        myName = name;
        max = theMax;
    }

    /**
     * Find out if a player is the max player.
     * @return true is max, false if min
     */
    public boolean isMax(){
        return max;
    }

    /**
     * Getter for AI_STATUS Status
     * @return
     */
    public boolean isAI(){
        return myAI;
    }

    /**
     * Get the player's piece
     * @return the char representation of the player's piece
     */
    public char getPiece(){
        return myPiece;
    }

    /**
     * Get the Player's name.
     * @return the string representation of the player's name
     */
    public String getName(){
        return myName;
    }

    /**
     * Overriden toString
     * @return ai status, name and if player is max
     */
    @Override
    public String toString(){
        return "\nNAME: " + myName + " AI: " + myAI +  " MAX: " + max + " PIECE: " + myPiece;
    }
}
