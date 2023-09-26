/**
 *  this class is the randomizer class that has been provided
 * throughout the course
 */
import java.util.*;

public class Randomizer{

    public static Random theInstance = null;

    public Randomizer(){

    }

    public static Random getInstance(){
        if(theInstance == null){
            theInstance = new Random();
        }
        return theInstance;
    }

    public static int nextInt(int n){
        return Randomizer.getInstance().nextInt(n);
    }

    // Return a number between min and max, inclusive. //
    public static int nextInt(int min, int max){
        return min + Randomizer.nextInt(max - min + 1);
    }

}