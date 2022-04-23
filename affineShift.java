package Ciphers;
import java.util.Scanner;
/***********************************************
 * affineShift takes a message, multiplicative shift, 
 * and additive shift for encryption and decryption.
 * 
 * @author Jochen Tamang
 * @version Winter 2022
 **********************************************/
public class affineShift {
    public static void main(String[] args)
    {
        //Scanner object to take user input
        Scanner s = new Scanner(System.in);
        System.out.print("Enter the message for encoding: ");
        //message for encryption (all uppercase and all alphabets)
        String message = s.nextLine().toUpperCase().replaceAll("[^a-zA-Z]", "");
        System.out.print("Enter the additive shift: ");
        //additive shift
        int aShift = s.nextInt();
        System.out.print("Enter the multiplicative shift: ");
        //multiplicative shift
        int mShift = s.nextInt();
        //if multiplicative shift is not invertible, user has to put a // new multiplicative shift.
        while(multInverse(mShift, 26) == 1)
        {
            System.out.print(mShift + " has no multiplicative inverse. Enter again: ");
            mShift = s.nextInt();
        }
        //the encrypted message from encoded method
        String encrypted = encode(message, mShift, aShift);
        System.out.println("Here is your encrypted message: " + encrypted);
        //outputs the decrypted message from decode() method
        System.out.println("Here is the decoded message: " + decode(encrypted,mShift,aShift));
        
    }
    /**
     * Takes encrypted message, multiplicative shift, and additive shift. Returns the decrypted message.
     * @param cipher --> message
     * @param mShift --> multiplicative shift
     * @param aShift --> additive shift
     * @return decoded String (message)
     */
    public static String decode(String cipher, int mShift, int aShift)
    {
        //variable that is returned later with decrypted message
        String decrypted = "";
        //loops through each character in the cipher String
        for(int i = 0; i < cipher.length();i++)
        {
            //maps urrent letter to current
            char current = cipher.charAt(i);
            //ascii basically tracks if A = 0, B = 1, etc. In ASCII, 65 = 'A'. So, ascii for A is 65 - 65 = 0
            int ascii = current - 65;
            //temporary variable tracks ascii - the additive shift used in encrypted mod by 26.
            int temp =  ((ascii - aShift) % 26);
            //if temp is a negative number, then temp loops back around to a positive number
            if(temp < 0) temp = temp + 26;
            //after undoing additive shift, current holds the character //after undoing multiplicative shift. Adding 65 gives us    //the ascii letter.
            //Equation: (multiplicativeShiftInverse * temp) % 26 = 1; 
            current =  (char) (((temp * multInverse(mShift, 26)) % 26) + 65);
            //adds that decrypted letter to decrypted String
            decrypted+=current;
        }
        //returns decrypted string after it goes through each letter in cipher text
        return decrypted;
        
    }
    /**
     * Takes message, multiplicative shift, and additive shift, and    * returns the encrypted message
     * @param message message that will get encrypted
     * @param mShift additive shift
     * @param aShift multiplicative shift
     * @return encrypted message
     */
    public static String encode(String message, int mShift, int aShift)
    {
        String encodedMessage = "";
        //loops through message character by character
        for(int i = 0; i < message.length(); i++)
        {
            //current character
            char current = message.charAt(i);
            //ascii is used for if A = 0, B = 2, etc. 'A' in ASCII is 65, so 65 - 65 = 0, which will represent A.
            int ascii = current - 65;
            //multiply the ascii variable by multiplicative shift mod 26
            int temp = (((ascii * mShift) % 26));
            // after multiplicative shift, adds the aShift to the variable, and adds 65 to get the ASCII representation
            current = (char)  (((temp + aShift) % 26) + 65);
            //adds the letter to encodedMessage string
            encodedMessage+=current;
        }
        return encodedMessage;
    }
    /**
     * a % b
     * @param a what is being modded
     * @param b the modulo
     * @return multiplicative inverse of a mod b.
     *         (a * return variable) + (b * some int) = 1
     */
    static int multInverse(int a, int b) 
    { 
        a = a % b; 
        //while x is less than the modulo, checks all number less than the modulo
        for (int x = 1; x < b; x++) 
        {
        // if some number * a mod by b is 1, then we have found the multiplicative inverse.
            if ((a * x) % b == 1) 
                return x; 
        }
        // if there is no mult inverse, then a is a divisor or factor of b. 
        return 1; 
    } 
    
}
