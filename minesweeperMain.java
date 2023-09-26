/*
this is the main class for the program and is in charge
of the actual game play component of minesweeper

it contains game loops,input methods and figures out when 
a win/loss occurs, as well as letting the player start a new game

*/
import java.util.Scanner;
public class minesweeperMain
{
    //for first move where tile is automatically revealed
    public static boolean firstMove = true;
    //variable for number of flags remaining
    public static int flagsRemaining;//************************************************************
    public static boolean victory;
    public static Grid grid;

    /**
     * main run method for program
     *
     * prints Welcome message and starting game map
     * gets initial user input
     * creates new grid object
     * starts game loop
     * handles victory or loss
     */
    public void run()
    {
        Scanner scnr = new Scanner(System.in);
        //Welcome Message + blank map based on no values
        System.out.println("WELCOME TO MINESWEEPER!\n");

        //************************************************************
        String Size;
        int newSize;
        while(true)
        {
            System.out.println("Small('S'), Medium('M'), Large('L')?: ");
            Size = scnr.nextLine();
            Size.toLowerCase();
            System.out.println();

            if(Size.equals("s"))
            {
                newSize = 5;
            }
            else if(Size.equals("m"))
            {
                newSize = 7;
            }
            else if(Size.equals("l"))
            {
                newSize = 10;
            }
            else
            {
                System.out.println("Invalid Input. Try Again. ");
                System.out.println();
                continue;
            }
            break;
        }
        Grid.setSize(newSize);
        flagsRemaining = Grid.NUMBOMBS;
        //************************************************************

        //this part is in an infinite while loop so that the user can restart
        //the game after a win/loss
        while(true)
        {
            //prints a blank game board
            System.out.println(startingMap());

            //extracts x and y coordinates from getInput() method
            int[] tempArray = getInput();
            int tempX = tempArray[0];
            int tempY = tempArray[1];

            //creates new grid object, passing in x, y coordinates
            //and prints it using the Grid toString method
            grid = new Grid(tempX, tempY);
            System.out.println(grid + "\n");

            //begins main game loop that returns true/false to 'victory'
            victory = gameLoop(grid);

            //Victory/Game Over messages
            if (victory)
            {
                System.out.println("\nCONGRATULATIONS!\n   YOU WIN!");
            }
            else
            {
                System.out.println("\nYOU DUG UP A BOMB\nGAME OVER!");
            }

            //print fully revealed game board
            System.out.println("\n" + grid.toStringRevealed(victory));

            //print play again message, any input will start new game
            System.out.println("\nPLAY AGAIN?...\n\n");
            String repeat = scnr.nextLine();

            //resets variables for new game
            firstMove = true;
            flagsRemaining = Grid.NUMBOMBS;
            Box.numRevealed = 0;

        }
    }


    /**
     * allows looped gameplay, takes grid object parameter
     *
     * @param grid, Grid Object
     * @return boolean, whether or not a victory/loss occurs
     */
    public boolean gameLoop(Grid grid)
    {
        //initialize variables for gameloop 
        int[] inputs = new int[3];
        int x = 0;
        int y = 0;
        boolean flag = false;
        //main gameloop
        while(true)
        {
            //calls getInput() method that returns 3 indice list of
            //inputs to 'inputs' variable
            inputs = getInput();
            //extract x,y coordinates from 'inputs'
            x = inputs[0];
            y = inputs[1];
            //if the selection is a revealed tile the player must reenter
            if (grid.getBox(x, y).isRevealed())
            {
                System.out.println("That tile is Inactive!");
                continue;
            }
            //checks to see if user wants to place a flag or dig up tile
            if(inputs[2] == 1)
            {
                flag = true;
            }
            else
            {
                flag = false;
            }
            //performs specified action
            handleMove(x, y, flag, grid);
            //makes sure there are no lone blank tiles
            Grid.chainRevealBlanks();
            //prints updated game board to console
            System.out.println(grid + "\n");

            //check for a win/loss and return true/false respectively
            if(grid.getBox(x, y).isRevealed() && grid.getBox(x, y).isBomb())
            {
                return false;
            }
            if(Grid.GRIDSIZE*Grid.GRIDSIZE-Grid.NUMBOMBS <= Box.numRevealed)
            {
                return true;
            }
            //game loop repeats
        }
    }

    /**
     * get user input and reject invalid inputs
     * lets the user re-enter if invalid
     *
     * Input includes x, y, coordinates and flag or reveal
     *
     * @return int[], array of inputs
     */
    public int[] getInput()
    {
        Scanner scnr = new Scanner(System.in);
        int flag = 0;//0 for false, 1 for true
        //Tells user how many flags are still available
        System.out.println("\nFlags Remaining: " + flagsRemaining);

        //calls 'getCoordinates()' method that returns 2 index
        //array of x,y coordinates to'coordinates' variable
        int[] coordinates = getCoordinates();

        //if it is the first move of the game it will only
        //return the coordinates and set 'firstMove' to false
        if (firstMove)
        {
            System.out.println("\n");
            firstMove = false;
            return coordinates;
        }

        //gets action from user and rejects invalid inputs
        while (true)
        {
            System.out.println("FLAG or DIG UP(F or D): ");
            String action = scnr.nextLine();
            if (action.equalsIgnoreCase("F"))
            {
                flag = 1;
                break;
            }
            else if (action.equalsIgnoreCase("D"))
            {
                flag = 0;
                break;
            }
            else
            {
                System.out.println("Invalid Input");
                continue;
            }
        }
        System.out.println("\n");
        //return 3 index array of all inputs
        int[] inputs = {coordinates[0], coordinates[1], flag};
        return inputs;
    }

    /**
     * get coordinates and reject invalid inputs
     * lets the user re-enter if invalid
     *
     * Input includes x, y, coordinates
     *
     * @return int[], array of coordinates
     */
    public int[] getCoordinates()
    {
        Scanner scnr = new Scanner(System.in);
        //initialize variables coordinate entry
        String input = "";
        String numberPortion = "";
        int x = 0;
        int y = 0;
        //while loop lets user reenter coordinates if invalid
        while(true)
        {
            //try & catch catches unexpected errors
            try
            {
                //gets coordinates from user in the form of a string
                System.out.println("Enter the Coordinate(eg. B3): ");
                input = scnr.nextLine();

                //assigns x variable to letter in input, ignoring case
                //and converts it to an integer(a-0, b-1, etc.)
                x = (int)(Character.toLowerCase(input.charAt(0)))-97;

                //assigns y variable to integer in input
                numberPortion = input.substring(1, input.length());
                y = Integer.parseInt(numberPortion);
                y--;

                //rejects input if it out of range
                if (x < 0 || x >= Grid.GRIDSIZE || y < 0 || y >= Grid.GRIDSIZE)
                {
                    System.out.println("INVALID INPUT!");
                    continue;
                }
                //break loop if input is valid
                break;

            }
            catch(Exception IllegalArgumentException)
            {
                System.out.println("INVALID INPUT!");
                continue;
            }
        }
        //return 2 index array of x,y coordinates
        int[] coordinates = {x, y};
        return coordinates;
    }

    /**
     * reacts to user input and adjusts selected box object
     *
     * @param x, x coordinate
     * @param y, y coordinate
     * @param placedFlag, whether or not the user placed a flag
     * @param grid, Grid object
     */
    public void handleMove(int x, int y, boolean placedFlag, Grid grid)
    {
        //gets box that corresponds with user input and assigns it to 'choice'
        Box choice = grid.getBox(x, y);

        //places flag/removes flag/reveals box
        if(placedFlag)
        {
            if (choice.isFlagged())
            {
                choice.removeFlag();
                flagsRemaining ++;
            }
            else
            {
                choice.putFlag();
                flagsRemaining --;
            }
        }
        else
        {
            choice.revealBox();
        }
    }

    /**
     * Print a blank Map to screen, doesn't reflect any array contents 
     *
     * return String, blank starting game map
     */
    public String startingMap()
    {
        //initialize variables for method
        String gridString = " 1 | ";
        String state = "";
        int row = 1;
        //begin nested for loop structure
        for(int i=0; i<Grid.GRIDSIZE; i++)
        {
            //prints 'grass' for each row
            for(int j=0; j<Grid.GRIDSIZE; j++)
            {
                gridString += Grid.grass + " ";
            }

            //this section prints the integers on the left side of the board
            if (row < Grid.GRIDSIZE)
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
        //prints the letters at the bottom of the board
        gridString += "\n    ";
        for(int i=1; i<=Grid.GRIDSIZE; i++)
        {
            gridString += "---";
        }
        gridString += "\n     ";
        for(int i=65; i<65+Grid.GRIDSIZE; i++)
        {
            gridString += (char)i + "  ";
        }
        return gridString + "\n";
    }
}