/*
this class has everything to do with box objects which represent
the individual tiles that make up the 2D grid

it creates new box objects with unique and default parameters,
it can return any attributes of a given box object, it can
alter the attributes of a given box object, and it keeps track
of how many boxes have been revealed

*/
public class Box
{
    //Instance Variables
    private boolean isBomb;
    private int xPos;
    private int yPos;
    private int numBombsAround;
    private boolean isFlagged = false;
    private boolean isRevealed = false;
    public boolean isReserved = false;
    //static variable tracks number of revealed boxes
    public static int numRevealed = 0;

    /**
     * Constructor Method
     *
     * @param xPosition, x coordinate of object
     * @param yPosition, y coordinate of object
     * @param isItABomb, whether or not object is a bomb
     */
    public Box(int xPosition, int yPosition, boolean isItABomb)
    {
        this.xPos = xPosition;
        this.yPos = yPosition;
        this.isBomb = isItABomb;
    }

    //getter returns x Position of object
    public int getXPos()
    {
        return xPos;
    }

    //getter returns y Position of object
    public int getYPos()
    {
        return yPos;
    }

    //getter returns whether or not object is a bomb
    public boolean isBomb()
    {
        return isBomb;
    }

    //getter returns whether or not object is reserved
    public boolean isReserved()
    {
        return isReserved;
    }

    //getter returns whether or not object is a bomb
    public boolean isRevealed()
    {
        return isRevealed;
    }

    //getter returns whether or not object is a bomb
    public boolean isFlagged()
    {
        return isFlagged;
    }

    //getter returns the number of bombs around a box object
    public int getNumBombsAround()
    {
        return numBombsAround;
    }

    //setter sets isBomb boolean variable to true
    public void placeBomb()
    {
        this.isBomb = true;
    }

    //setter sets the number of bombs around a box object, given a parameter
    public void setNumBombsAround(int num)
    {
        numBombsAround = num;
    }

    //setter reserves box to not be a bomb
    public void reserveBox()
    {
        isReserved = true;
    }

    //setter sets isRevealed boolean variable to true
    public void revealBox()
    {
        if(!isRevealed)
        {
            isRevealed = true;
            numRevealed ++;
        }
    }

    //setter sets isFlagged boolean variable to true
    public void putFlag()
    {
        isFlagged = true;
    }

    //setter sets isFlagged boolean variable to false
    public void removeFlag()
    {
        isFlagged = false;
    }
}