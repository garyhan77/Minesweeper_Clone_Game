/*
this class is in charge of everything to do with the grid
which is a 2D array of Box objects

it creates new grid objects, it can return box objects based
on x, y coordinates, it contains methods that allow special in
game mechanics, and it can print out the grid to the console
using emojis to reflect the current game state, as well as a 
fully revealed grid

*/
public class Grid
{
    //static variables 
    //************************************************************
    public static int GRIDSIZE;//must be > 5 and < 26//6 is a good size
    public static int NUMBOMBS;// = (GRIDSIZE*GRIDSIZE)/5;
    //************************************************************
    public static String grass = "❎";
    public static String[] boxedNumbers= {"⏹","1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣"};
    private static Box[][] grid;//************************************************************


    /**
     * Contructor Method, adds box objects to each index
     * places bombs in random locations in 2d array using
     * the placeBomb() box method
     *
     * @param firstX, x coordinate of first move
     * @param firstY, y coordinate of first move
     */
    public Grid(int firstX, int firstY)
    {
        //adds a blank object to each spot in the grid 2d array
        for(int y=0; y<GRIDSIZE; y++)
        {
            for(int x=0; x<GRIDSIZE; x++)
            {
                Box box = new Box(x, y, false);
                grid[y][x] = box;
            }
        }

        //reveals 9 boxs around input and reserves them from being bombs
        for(int vert=0; vert<3; vert++)
        {
            for(int horiz=0; horiz<3; horiz++)
            {
                try
                {
                    grid[firstY-1+vert][firstX-1+horiz].reserveBox();
                    grid[firstY-1+vert][firstX-1+horiz].revealBox();
                }
                catch(Exception ArrayIndexOutOfBoundsException)
                {
                    continue;
                }
            }
        }

        int i = 0;//sentinel for while loop
        while(i<NUMBOMBS)
        {
            //creates new random coordinates
            int randomX = Randomizer.nextInt(0, GRIDSIZE-1);
            int randomY = Randomizer.nextInt(0, GRIDSIZE-1);

            //only places a bomb is the box isn't already
            //a bomb or reserved
            if (!(grid[randomY][randomX].isBomb()) && !(grid[randomY][randomX].isReserved()))
            {
                grid[randomY][randomX].placeBomb();
                i++;
            }
        }
        //call findNums method to assign each box with the 
        //number of bombs around it
        findNums();
        chainRevealBlanks();
    }

    //return the box object at a given index in 2d array object
    public Box getBox(int x, int y)
    {
        return grid[y][x];
    }

    /**
     * reveals chunks of map, ensuring that there isn't a visible
     * box with 0 bombs, next to a hidden box with 0 bombs around
     */
    public static void chainRevealBlanks()
    {
        //running set to true so that loop begins 
        boolean running = true;
        while(running)
        {
            for(Box[] arr : grid)
            {
                //running set to false so that loop doesn't break
                //in if statement after following for loop
                running = false;

                for(Box box : arr)
                {
                    ///reveals 8 boxes around blank spaces only
                    if(box.getNumBombsAround()==0 && box.isRevealed() && !box.isBomb())
                    {
                        running = revealAround(box.getXPos(), box.getYPos());
                        //break out of loop is change occurs
                        if(running)
                        {
                            break;
                        }
                    }
                }
                //break out of loop if change occurs
                if(running)
                {
                    break;
                }
            }
        }
    }

    /**
     * this method is designed to compliment the chainRevealBlanks
     * reveals 8 boxes around a givin point
     *
     * @param x, x coordinate
     * @param y, y coordinate
     *
     * @return boolean, true if change occurs, false if nothing occurs
     */
    public static boolean revealAround(int x, int y)
    {
        //nested for loop begins
        for(int vert=0; vert<3; vert++)
        {
            for(int horiz=0; horiz<3; horiz++)
            {
                try
                {
                    //reveals box and returns true if the box has
                    //zero bombs around, and isn't revealed already
                    if(grid[y-1+vert][x-1+horiz].getNumBombsAround()==0 &&
                            !(grid[y-1+vert][x-1+horiz].isRevealed()))
                    {
                        grid[y-1+vert][x-1+horiz].revealBox();
                        return true;
                    }
                    //reveals box
                    grid[y-1+vert][x-1+horiz].revealBox();
                }
                //catches out of bounds exception
                catch(Exception ArrayIndexOutOfBoundsException)
                {
                    continue;
                }
            }
        }
        //returns false if there aren't any more lone zero bomb tiles
        return false;
    }

    /**
     * finds the number of bombs around a given box, excluding boxes
     * with bombs, and sets NumBombsAround variable in Box method
     */
    private void findNums()
    {
        for(int y=0; y<GRIDSIZE; y++)
        {
            for(int x=0; x<GRIDSIZE; x++)
            {
                if(!(grid[y][x].isBomb()))
                {
                    grid[y][x].setNumBombsAround(checkSurround(x, y));
                }
            }
        }
    }

    /**
     * count number of bombs around a given box in 2d array and return result
     *
     * @param x, x coordinate
     * @param y, y coordinate
     *
     * @return int, number of bombs around a box
     */
    private int checkSurround(int x, int y)
    {
        int counter = 0;

        for(int vert=0; vert<3; vert++)
        {
            for(int horiz=0; horiz<3; horiz++)
            {
                try
                {
                    if(grid[y-1+vert][x-1+horiz].isBomb())
                    {
                        counter++;
                    }
                }
                catch(Exception ArrayIndexOutOfBoundsException)
                {
                    continue;
                }
            }
        }
        return counter;
    }

    //************************************************************
    public static void setSize(int size)
    {
        GRIDSIZE = size;
        NUMBOMBS = (size*size)/5;
        grid = new Box[size][size];
    }


    /**
     * returns string that shows the current game progress
     *
     * @return String, map that reflects current game state
     */
    public String toString()
    {
        //initialize method variable
        String gridString = " 1 | ";
        String state = "";
        int row = 1;

        //iterates through grid
        for(Box[] arr : grid)
        {
            for(Box box : arr)
            {
                if (box.isRevealed())
                {
                    if(!box.isBomb())
                    {
                        //assigns revealed tiles that aren't bombs an int emoji
                        state =  "" + boxedNumbers[box.getNumBombsAround()] ;
                    }
                    else
                    {
                        //assigns bombs a black circle emoji
                        state = "💣";
                    }
                }
                else if(box.isFlagged())
                {
                    //assigns flagged tiles a radioactive emoji
                    state = "🚩";
                }
                else
                {
                    //assigns unrevealed/unflagged tiles with emojis that
                    //represent grass
                    state = grass;
                }
                //adds state variable to gridString
                gridString += state + " ";
            }

            //prints number on left side of board
            if (row < GRIDSIZE)
            {
                row ++;
                if (row <= 9)
                {
                    gridString += "\n "+row+" | ";
                }
                else if (row <= 99)
                {
                    gridString += "\n"+row+" | ";
                }
                else
                {
                    gridString += "\n"+row+"| ";
                }

            }
        }
        //prints letters at bottom of the board
        gridString += "\n    ";
        for(int i=1; i<=GRIDSIZE; i++)
        {
            gridString += "---";
        }
        gridString += "\n     ";
        for(int i=65; i<65+GRIDSIZE; i++)
        {
            gridString += (char)i + "  ";
        }
        return gridString;
    }

    /**
     * returns string that shows the fully revealed game board
     *
     * @return String, map that shows all boxes revealed
     */
    public String toStringRevealed(boolean victory)
    {
        //initializes method variables
        String gridString = " 1 | ";
        String state = "";
        int row = 1;

        //iterate through grid
        for(Box[] arr : grid)
        {
            for(Box box : arr)
            {
                if (box.isBomb())
                {
                    if(victory)
                    {
                        //assigns bombs green circle emoji if victory
                        state = "✅";
                    }
                    else
                    {
                        //assigns bombs green circle emoji if lose
                        state = "💣";
                    }
                }
                else
                {
                    //assigns all other tiles as integer emoji
                    state = boxedNumbers[box.getNumBombsAround()] + "";
                }

                //adds state variable to gridString
                gridString += state + " ";
            }

            //prints numbers on left side of board
            if (row < GRIDSIZE)
            {
                row ++;
                if (row <= 9)
                {
                    gridString += "\n "+row+" | ";
                }
                else if (row <= 99)
                {
                    gridString += "\n"+row+" | ";
                }
                else
                {
                    gridString += "\n"+row+"| ";
                }

            }
        }

        //prints letters at the bottom of board
        gridString += "\n    ";
        for(int i=1; i<=GRIDSIZE; i++)
        {
            gridString += "---";
        }
        gridString += "\n     ";
        for(int i=65; i<65+GRIDSIZE; i++)
        {
            gridString += (char)i + "  ";
        }
        return gridString;
    }
}